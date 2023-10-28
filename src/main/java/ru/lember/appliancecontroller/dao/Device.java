package ru.lember.appliancecontroller.dao;

import lombok.Data;

@Data
public class Device {
    private Long id;
    private String name;
    private String type;
    private Long locationId;
}

