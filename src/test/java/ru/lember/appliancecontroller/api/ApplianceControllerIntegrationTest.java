package ru.lember.appliancecontroller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ru.lember.appliancecontroller.api.model.CommandRequest;
import ru.lember.appliancecontroller.api.model.Device;
import ru.lember.appliancecontroller.api.model.DeviceState;
import ru.lember.appliancecontroller.api.model.WashingDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplianceControllerIntegrationTest {

    public static final long NON_EXISTENT_DEVICE_ID = 999;
    public static final long EXISTING_DEVICE_ID = 1;
    public static final long EXISTING_PROGRAM_ID = 5;
    public static final long NON_EXISTING_PROGRAM_ID = 50;
    public static final String TEST_CORRELATION_ID = "test-correlation-id";
    public static final WashingDetails DETAILS = new WashingDetails(false, true);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllDevices() {
        String url = "http://localhost:" + port + "/api/v1/device/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("LEMBER_CORRELATION_ID", TEST_CORRELATION_ID);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<Device>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testGetAllDevicesWithoutCorrelationId() {
        String url = "http://localhost:" + port + "/api/v1/device/";
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetNonExistentDevice() {
        String url = "http://localhost:" + port + "/api/v1/device/" + NON_EXISTENT_DEVICE_ID;
        HttpHeaders headers = new HttpHeaders();
        headers.set("LEMBER_CORRELATION_ID", TEST_CORRELATION_ID);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testGetExistingDevice() {
        String url = "http://localhost:" + port + "/api/v1/device/" + EXISTING_DEVICE_ID;
        HttpHeaders headers = new HttpHeaders();
        headers.set("LEMBER_CORRELATION_ID", TEST_CORRELATION_ID);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Device> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Device.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testGetDeviceWithoutCorrelationId() {
        String url = "http://localhost:" + port + "/api/v1/device/" + EXISTING_DEVICE_ID;
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testChangeStateWithoutCorrelationId() {
        String url = "http://localhost:" + port + "/api/v1/command";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        CommandRequest request = new CommandRequest(EXISTING_DEVICE_ID, DeviceState.READY, EXISTING_PROGRAM_ID, null);

        HttpEntity<CommandRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testChangeStateWithoutDeviceId() {
        String url = "http://localhost:" + port + "/api/v1/command";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("LEMBER_CORRELATION_ID", TEST_CORRELATION_ID);

        CommandRequest request = new CommandRequest(null, DeviceState.READY, EXISTING_PROGRAM_ID, null);

        HttpEntity<CommandRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testChangeStateWithNonExistedDeviceId() {
        String url = "http://localhost:" + port + "/api/v1/command";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("LEMBER_CORRELATION_ID", TEST_CORRELATION_ID);

        CommandRequest request = new CommandRequest(NON_EXISTENT_DEVICE_ID, DeviceState.READY, EXISTING_PROGRAM_ID, null);

        HttpEntity<CommandRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testChangeStateWithoutProgramId() {
        String url = "http://localhost:" + port + "/api/v1/command";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("LEMBER_CORRELATION_ID", TEST_CORRELATION_ID);

        CommandRequest request = new CommandRequest(EXISTING_DEVICE_ID, DeviceState.READY, null, DETAILS);

        HttpEntity<CommandRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testChangeStateWithNonExistingProgram() {
        String url = "http://localhost:" + port + "/api/v1/command";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("LEMBER_CORRELATION_ID", TEST_CORRELATION_ID);

        CommandRequest request = new CommandRequest(EXISTING_DEVICE_ID, DeviceState.READY, NON_EXISTING_PROGRAM_ID, DETAILS);

        HttpEntity<CommandRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testChangeStateWithIncorrectState() {
        String url = "http://localhost:" + port + "/api/v1/command";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("LEMBER_CORRELATION_ID", TEST_CORRELATION_ID);

        CommandRequest request = new CommandRequest(EXISTING_DEVICE_ID, DeviceState.WASHING, EXISTING_PROGRAM_ID, DETAILS);

        HttpEntity<CommandRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testChangeAllPossibleTransitions() {
        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.NOT_ACTIVE);

        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.NOT_ACTIVE);

        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.SPINNING);
        checkStateTransition(DeviceState.NOT_ACTIVE);

        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.SPINNING);
        checkStateTransition(DeviceState.DRAINING);
        checkStateTransition(DeviceState.NOT_ACTIVE);

        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.PAUSED);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.SPINNING);
        checkStateTransition(DeviceState.PAUSED);
        checkStateTransition(DeviceState.SPINNING);
        checkStateTransition(DeviceState.DRAINING);
        checkStateTransition(DeviceState.PAUSED);
        checkStateTransition(DeviceState.DRAINING);
        checkStateTransition(DeviceState.FINISHED);
        checkStateTransition(DeviceState.NOT_ACTIVE);

        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.SPINNING);
        checkStateTransition(DeviceState.DRAINING);
        checkStateTransition(DeviceState.FINISHED);

        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.CANCELLED);
        checkStateTransition(DeviceState.NOT_ACTIVE);

        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.SPINNING);
        checkStateTransition(DeviceState.CANCELLED);
        checkStateTransition(DeviceState.NOT_ACTIVE);

        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.SPINNING);
        checkStateTransition(DeviceState.DRAINING);
        checkStateTransition(DeviceState.CANCELLED);
        checkStateTransition(DeviceState.NOT_ACTIVE);

        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.PAUSED);
        checkStateTransition(DeviceState.NOT_ACTIVE);

        checkStateTransition(DeviceState.READY);
        checkStateTransition(DeviceState.WASHING);
        checkStateTransition(DeviceState.PAUSED);
        checkStateTransition(DeviceState.CANCELLED);
    }

    private void checkStateTransition(DeviceState transitionState) {
        String url = "http://localhost:" + port + "/api/v1/command";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("LEMBER_CORRELATION_ID", TEST_CORRELATION_ID);
        CommandRequest request = new CommandRequest(EXISTING_DEVICE_ID, transitionState, EXISTING_PROGRAM_ID, DETAILS);
        HttpEntity<CommandRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String deviceStateUrl = "http://localhost:" + port + "/api/v1/device/{deviceId}";
        ResponseEntity<Device> deviceStateResponse = restTemplate.exchange(deviceStateUrl, HttpMethod.GET, requestEntity, Device.class, request.getDeviceId());
        assertEquals(HttpStatus.OK, deviceStateResponse.getStatusCode());
        assertNotNull(deviceStateResponse.getBody());
        assertEquals(transitionState, deviceStateResponse.getBody().getState());
    }
}
