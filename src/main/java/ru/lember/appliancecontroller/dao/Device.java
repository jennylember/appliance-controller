package ru.lember.appliancecontroller.dao;

import lombok.Data;

import java.time.Instant;

@Data
public class Device {
    private Long id;
    private String name;
    private String model;
    private Instant addDate;
    private Instant modificationDate;
}