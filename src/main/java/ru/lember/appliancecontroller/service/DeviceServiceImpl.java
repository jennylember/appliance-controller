package ru.lember.appliancecontroller.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lember.appliancecontroller.api.model.*;
import ru.lember.appliancecontroller.dao.State;
import ru.lember.appliancecontroller.mapper.ActiveStateMapper;
import ru.lember.appliancecontroller.mapper.DeviceMapper;
import ru.lember.appliancecontroller.mapper.StateMapper;
import ru.lember.appliancecontroller.metrics.MetricsService;
import ru.lember.appliancecontroller.properties.BusinessProperties;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    private final BusinessProperties properties;
    private final DeviceMapper deviceMapper;
    private final ActiveStateMapper activeStateMapper;
    private final StateMapper stateMapper;
    private final StateTransitions transitions;
    private final MetricsService metricsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void postConstruct() {
        log.info("initialized. properties: {}", properties);
    }

    @Autowired
    public DeviceServiceImpl(
            BusinessProperties properties,
            DeviceMapper deviceMapper,
            ActiveStateMapper activeStateMapper,
            StateMapper stateMapper,
            StateTransitions transitions,
            MetricsService metricsService
    ) {
        this.properties = properties;
        this.deviceMapper = deviceMapper;
        this.activeStateMapper = activeStateMapper;
        this.stateMapper = stateMapper;
        this.transitions = transitions;
        this.metricsService = metricsService;
    }

    @Override
    public List<Device> getDevices() {
        try {
            var daoDevices = deviceMapper.getAllDevices();
            return daoDevices.stream()
                    .map(this::getDevice)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            metricsService.incrementUnexpectedError(e.getMessage());
            throw e;
        }
    }

    @Override
    public Device getDevice(Long id) {
        try {
            return getDevice(deviceMapper.getDeviceById(id));
        } catch (Exception e) {
            metricsService.incrementUnexpectedError(e.getMessage());
            throw e;
        }
    }

    private Device getDevice(ru.lember.appliancecontroller.dao.Device daoDevice) {

        var model = daoDevice.getModel();

        metricsService.incrementDevice(model);

        var programIds = properties.getProgramIdsByModel(model);
        var programs = properties.getPrograms().stream()
                .filter(p -> programIds.contains(p.getId()))
                .map(propertiesProgram -> new Program(
                        propertiesProgram.getId(),
                        propertiesProgram.getName(),
                        DeviceType.fromValue(propertiesProgram.getDeviceType().toString()),
                        propertiesProgram.isPreWashSupported(),
                        propertiesProgram.isQuickSupported(),
                        propertiesProgram.getDurationMs())
                )
                .collect(Collectors.toSet());

        var daoState = activeStateMapper.getActiveStateName(daoDevice.getId());
        var state = DeviceState.fromValue(daoState.toString());

        return new Device(
                daoDevice.getId(),
                daoDevice.getName(),
                model,
                programs,
                state,
                transitions.getAvailableStates(daoState).stream()
                        .map(s -> DeviceState.fromValue(s.toString()))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public CommandServiceResult command(Long deviceId, DeviceState targetDeviceState, Long programId, WashingDetails details) {

        try {
            var daoDevice = deviceMapper.getDeviceById(deviceId);
            if (daoDevice == null) {
                return new CommandServiceResult(false, "Device not found, deviceId = " + deviceId);
            }
            var model = daoDevice.getModel();
            metricsService.incrementCommand(model, targetDeviceState.toString());

            ru.lember.appliancecontroller.dao.DeviceState activeState = activeStateMapper.getActiveStateName(deviceId);
            Set<ru.lember.appliancecontroller.dao.DeviceState> availableStates = transitions.getAvailableStates(activeState);

            ru.lember.appliancecontroller.dao.DeviceState daoDeviceState = ru.lember.appliancecontroller.dao.DeviceState.valueOf(targetDeviceState.toString());

            if (!availableStates.contains(daoDeviceState)) {
                return new CommandServiceResult(false, "Transition from " + activeState + " to " + targetDeviceState + " is not available");
            }

            if (properties.getPrograms().stream().noneMatch(program -> program.getId().equals(programId))) {
                return new CommandServiceResult(false, "Unsupported program, programId = " + programId);
            }

            if (details == null) {
                return new CommandServiceResult(false, "Missing details program, programId = " + programId);
            }

            State newState = new State();
            newState.setName(daoDeviceState);
            newState.setDeviceType(ru.lember.appliancecontroller.dao.DeviceType.WASHING_MACHINE);
            newState.setProgramId(programId);
            newState.setDetails(objectMapper.writeValueAsString(details));

            insertAndUpdate(newState, deviceId);

            var result = new CommandServiceResult(true, null);

            if (!result.isSuccess()) {
                metricsService.incrementUnexpectedError(result.getDetails());
            }

            return result;
        } catch (Exception e) {
            metricsService.incrementUnexpectedError(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void insertAndUpdate(State newState, Long deviceId) {
        try {
            stateMapper.addState(newState);
        } catch (Exception e) {
            throw new RuntimeException("Can't insert table states, state: " + newState, e);
        }
        try {
            int rowsAffected = activeStateMapper.updateActiveState(deviceId, newState.getId());
            if (rowsAffected <= 0) {
                throw new RuntimeException("Zero rows were updated table active_state, deviceId = " + deviceId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't update table active_state, deviceId = " + deviceId, e);
        }
    }
}
