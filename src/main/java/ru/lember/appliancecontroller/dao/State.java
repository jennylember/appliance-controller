package ru.lember.appliancecontroller.dao;

import lombok.Data;

import java.time.Instant;

@Data
public class State {
    private Long id;
    private DeviceState name;
    private DeviceType deviceType;
    private Long programId;
    private String details;
    private Instant endDate;
    private Instant addDate;
    private Instant modificationDate;
}
