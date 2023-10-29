package ru.lember.appliancecontroller.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public record MetricsService(MeterRegistry meterRegistry) {

    public void incrementCommand(String deviceModel, String targetState) {
        meterRegistry.counter("command", "model", deviceModel, "targetState", targetState).increment();
    }

    public void incrementDevice(String deviceModel) {
        meterRegistry.counter("devices", "model", deviceModel).increment();
    }

    public void incrementUnexpectedError(String err) {
        meterRegistry.counter("unexpected_error", "error", err).increment();
    }
}