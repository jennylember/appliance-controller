package ru.lember.appliancecontroller.service;


import ru.lember.appliancecontroller.api.model.Device;
import ru.lember.appliancecontroller.api.model.DeviceState;
import ru.lember.appliancecontroller.api.model.WashingDetails;

import java.util.List;

public interface DeviceService {

    List<Device> getDevices();
    Device getDevice(Long id);
    CommandServiceResult command(
            Long deviceId,
            DeviceState targetDeviceState,
            Long programId,
            WashingDetails details
    );

}
