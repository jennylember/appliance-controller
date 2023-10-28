package ru.lember.appliancecontroller.properties;

import lombok.Data;
import ru.lember.appliancecontroller.dao.DeviceType;

@Data
public class Program {
    private Long id;
    private DeviceType deviceType;
    private String name;
    private boolean isPrewashSupported;
    private boolean isQuickSupported;
    private Long durationMs;
}
