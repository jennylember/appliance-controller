package ru.lember.appliancecontroller.api.model;//package ru.lember.generated.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Program {

  private Long id;
  private String name;
  private DeviceType deviceType;
  private Boolean preWashSupported;
  private Boolean quickSupported;
  private Long durationMs;

}

