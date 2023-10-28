package ru.lember.appliancecontroller.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lember.appliancecontroller.dao.Device;
import ru.lember.appliancecontroller.mapper.DeviceMapper;
import ru.lember.appliancecontroller.properties.BusinessProperties;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class DeviceService {

    private final BusinessProperties properties;
    private final DeviceMapper deviceMapper;

    @PostConstruct
    private void postConstruct() {
        log.info("initialized. properties: {}", properties);
    }

    @Autowired
    public DeviceService(BusinessProperties properties, DeviceMapper deviceMapper) {
        this.properties = properties;
        this.deviceMapper = deviceMapper;
    }

    public Device getDeviceById(long id) {
        return deviceMapper.getDeviceById(id);
    }

    public List<Device> getAllDevices() {
        return deviceMapper.getAllDevices();
    }
}
