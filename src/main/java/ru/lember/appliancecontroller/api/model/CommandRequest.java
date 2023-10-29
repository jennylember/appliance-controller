package ru.lember.appliancecontroller.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class CommandRequest {

  private Long deviceId;
  private DeviceState targetDeviceState;
  private Long programId;
  private WashingDetails details;

}

