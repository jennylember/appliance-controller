package ru.lember.appliancecontroller.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lember.appliancecontroller.dao.Device;
import ru.lember.appliancecontroller.mapper.DeviceMapper;

import java.util.List;

@Service
public class DeviceService {

    private final DeviceMapper deviceMapper;

    @Autowired
    public DeviceService(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    public Device getDeviceById(long id) {
        return deviceMapper.getDeviceById(id);
    }

    public List<Device> getAllDevices() {
        return deviceMapper.getAllDevices();
    }
}
