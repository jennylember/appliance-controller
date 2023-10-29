package ru.lember.appliancecontroller.service;

import org.springframework.stereotype.Component;
import ru.lember.appliancecontroller.dao.DeviceState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StateTransitions {

    private static final Map<DeviceState, Set<DeviceState>> AVAILABLE_STATES = new HashMap<>();

    static {
        AVAILABLE_STATES.put(DeviceState.NOT_ACTIVE, Set.of(DeviceState.READY));
        AVAILABLE_STATES.put(DeviceState.READY, Set.of(DeviceState.WASHING, DeviceState.NOT_ACTIVE));
        AVAILABLE_STATES.put(DeviceState.WASHING, Set.of(DeviceState.SPINNING, DeviceState.PAUSED, DeviceState.CANCELLED, DeviceState.DRAINING, DeviceState.NOT_ACTIVE));
        AVAILABLE_STATES.put(DeviceState.CANCELLED, Set.of(DeviceState.NOT_ACTIVE));
        AVAILABLE_STATES.put(DeviceState.SPINNING, Set.of(DeviceState.DRAINING, DeviceState.PAUSED, DeviceState.CANCELLED, DeviceState.NOT_ACTIVE));
        AVAILABLE_STATES.put(DeviceState.DRAINING, Set.of(DeviceState.FINISHED, DeviceState.PAUSED, DeviceState.CANCELLED, DeviceState.NOT_ACTIVE));
        AVAILABLE_STATES.put(DeviceState.FINISHED, Set.of(DeviceState.READY, DeviceState.NOT_ACTIVE));
        AVAILABLE_STATES.put(DeviceState.PAUSED, Set.of(DeviceState.WASHING, DeviceState.SPINNING, DeviceState.DRAINING, DeviceState.CANCELLED, DeviceState.NOT_ACTIVE));
    }

    public Set<DeviceState> getAvailableStates(DeviceState currentState) {
        return Set.copyOf(AVAILABLE_STATES.get(currentState));
    }
}
