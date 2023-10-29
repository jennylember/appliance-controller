package ru.lember.appliancecontroller.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    private Long id;
    private String name;
    private String model;
    private Set<Program> supportedPrograms = new HashSet<>();
    private DeviceState state;
    private Set<DeviceState> availableCommands = new HashSet<>();

}

