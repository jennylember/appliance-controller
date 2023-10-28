package ru.lember.appliancecontroller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.lember.appliancecontroller.dao.Device;
import ru.lember.appliancecontroller.service.DeviceService;

import java.util.List;

@RestController
@RequestMapping("/appliance")
public class ApplianceController {

    private final DeviceService deviceService;

    @Autowired
    public ApplianceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/device/{id}")
    public Device getDeviceById(@PathVariable Long id) {
        return deviceService.getDeviceById(id);
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

}
