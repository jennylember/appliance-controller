package ru.lember.appliancecontroller.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DeviceState {

    NOT_ACTIVE("NOT_ACTIVE"),
    READY("READY"),
    CANCELLED("CANCELLED"),
    WASHING("WASHING"),
    PAUSED("PAUSED"),
    SPINNING("SPINNING"),
    DRAINING("DRAINING"),
    FINISHED("FINISHED");

    private final String value;

    DeviceState(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static DeviceState fromValue(String value) {
        for (DeviceState b : DeviceState.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

