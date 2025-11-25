package org.example.eventnotifier.controller;

import org.example.eventnotifier.model.EventRecord;
import org.example.eventnotifier.model.EventRequest;
import org.example.eventnotifier.model.EventType;
import org.example.eventnotifier.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    private final EventService eventService = mock(EventService.class);
    private final EventController controller = new EventController(eventService);

    @Test
    @DisplayName("Valid request calls service and returns 200 response")
    void validEventRequest() {
        EventRequest req = new EventRequest(
                "EMAIL",
                Map.of("recipient", "test@example.com", "msg", "Hello"),
                "http://localhost:8080/api/events/ack"
        );

        EventRecord dummyRecord = new EventRecord(
                EventType.EMAIL,
                req.payload(),
                req.callbackUrl()
        );

        when(eventService.acceptEvent(eq(EventType.EMAIL), eq(req.payload()), eq(req.callbackUrl())))
                .thenReturn(dummyRecord);

        var response = controller.generateEvent(req);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Event accepted for processing.", response.getBody().get("message"));
        verify(eventService, times(1))
                .acceptEvent(EventType.EMAIL, req.payload(), req.callbackUrl());
    }

    @Test
    @DisplayName("Invalid eventType returns 400")
    void invalidEventType() {
        EventRequest req = new EventRequest(
                "BADTYPE",
                Map.of(),
                "http://localhost:8080/api/events/ack"
        );

        var response = controller.generateEvent(req);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid eventType", response.getBody().get("error"));
        verify(eventService, never()).acceptEvent(any(), any(), any());
    }

    @Test
    @DisplayName("Callback /ack endpoint logs and returns 200")
    void ackEndpoint() {
        Map<String, Object> payload = Map.of("eventId", "1234");

        var response = controller.acknowledge(payload);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Acknowledged for processing!", response.getBody());
    }
}
