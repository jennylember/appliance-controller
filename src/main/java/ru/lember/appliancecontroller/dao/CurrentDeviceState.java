package ru.lember.appliancecontroller.dao;

import lombok.Data;

import java.time.Instant;

@Data
public class CurrentDeviceState {
    private Long id;
    private Long deviceId;
    private Long stateId;
    private Instant addDate;
    private Instant modificationDate;
}