package ru.lember.appliancecontroller.dao;

import lombok.Data;

import java.time.Instant;

@Data
public class ActiveState {
    private Long deviceId;
    private Long stateId;
    private Instant addDate;
    private Instant modificationDate;
}