package ru.lember.appliancecontroller.service;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class CommandServiceResult {

    private boolean isSuccess;
    private String details;

}
