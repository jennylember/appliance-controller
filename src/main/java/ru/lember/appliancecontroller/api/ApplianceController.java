package ru.lember.appliancecontroller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import ru.lember.appliancecontroller.api.model.CommandRequest;
import ru.lember.appliancecontroller.api.model.Device;
import ru.lember.appliancecontroller.metrics.MetricsService;
import ru.lember.appliancecontroller.service.DeviceService;
import ru.lember.appliancecontroller.service.DeviceServiceImpl;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@SuppressWarnings("unchecked")
public class ApplianceController {

    private final DeviceService deviceService;
    private final MetricsService metricsService;

    @Autowired
    public ApplianceController(final DeviceServiceImpl deviceService, MetricsService metricsService) {
        this.deviceService = deviceService;
        this.metricsService = metricsService;
    }

    @Operation(
            operationId = "command",
            summary = "Send Command to Device",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transition successful"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Device or Program not found or LEMBER_CORRELATION_ID header is missing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/command",
            consumes = {"application/json"}
    )
    ResponseEntity<String> command(
            @Parameter(name = "LEMBER_CORRELATION_ID", description = "Correlation ID", required = true, in = ParameterIn.HEADER)
            @RequestHeader(value = "LEMBER_CORRELATION_ID") String correlationId,
            @Parameter(name = "CommandRequest", description = "A method to send command to a particular device", required = true)
            @RequestBody final CommandRequest request
    ) {
        return executeSafe(() -> {
            if (correlationId == null || correlationId.isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            log.info("request: /command, correlationId: {}, request: {}", correlationId, request);

            if (ObjectUtils.isEmpty(request.getDeviceId())) {
                return ResponseEntity.badRequest().body("required field deviceId is missing");
            }

            if (ObjectUtils.isEmpty(request.getTargetDeviceState())) {
                return ResponseEntity.badRequest().body("required field targetDeviceId is missing");
            }

            var result = deviceService.command(request.getDeviceId(), request.getTargetDeviceState(), request.getProgramId(), request.getDetails());

            log.info("result: /command, correlationId: {}, result: {}", correlationId, result);

            return result.isSuccess()
                    ? ResponseEntity.ok().build()
                    : ResponseEntity.internalServerError().body(result.getDetails());
        });
    }


    @Operation(
            operationId = "device",
            summary = "Get Device",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns an info about a single Device", content = {
                            @Content(mediaType = "application/json")
                    }),
                    @ApiResponse(responseCode = "404", description = "Device not found"),
                    @ApiResponse(responseCode = "400", description = "LEMBER_CORRELATION_ID header is missing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/device/{deviceId}",
            produces = {"application/json"}
    )
    ResponseEntity<Device> device(
            @Parameter(name = "deviceId", description = "Device ID", required = true, in = ParameterIn.PATH)
            @PathVariable("deviceId") Long deviceId,
            @Parameter(name = "LEMBER_CORRELATION_ID", description = "Correlation ID", required = true, in = ParameterIn.HEADER)
            @RequestHeader(value = "LEMBER_CORRELATION_ID") String correlationId
    ) {
        return executeSafe(() -> {
            log.info("request: /device/{deviceId}, correlationId: {}, deviceId: {}", correlationId, deviceId);

            var result = deviceService.getDevice(deviceId);

            log.info("result: /device/{deviceId}, correlationId: {}, deviceId: {}, result: {}", correlationId, deviceId, result);

            return ResponseEntity.ok(result);
        });
    }

    @Operation(
            operationId = "devices",
            summary = "Get Devices",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns an info about all available Devices", content = {
                            @Content(mediaType = "application/json")
                    }),
                    @ApiResponse(responseCode = "400", description = "LEMBER_CORRELATION_ID header is missing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/device/",
            produces = {"application/json"}
    )
    ResponseEntity<List<Device>> devices(
            @Parameter(name = "LEMBER_CORRELATION_ID", description = "Correlation ID", required = true, in = ParameterIn.HEADER)
            @RequestHeader(value = "LEMBER_CORRELATION_ID") String correlationId
    ) {
        return executeSafe(() -> {
            log.info("request: /device, correlationId: {}", correlationId);

            var result = deviceService.getDevices();

            log.info("result: /device, correlationId: {}, result: {}", correlationId, result);

            return ResponseEntity.ok(result);
        });
    }

    @SuppressWarnings("rawtypes")
    private ResponseEntity executeSafe(Supplier<ResponseEntity> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            metricsService.incrementUnexpectedError(e.getMessage());
            return ResponseEntity.internalServerError().body("Unexpected server error: " + e.getMessage());
        }
    }
}
