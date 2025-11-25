package org.example.eventnotifier.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.eventnotifier.model.EventRecord;
import org.example.eventnotifier.model.EventRequest;
import org.example.eventnotifier.model.EventType;
import org.example.eventnotifier.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> generateEvent(@RequestBody EventRequest req) {
        EventType eventType;
        try {
            eventType = EventType.from(req.eventType());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid eventType"));
        }

        EventRecord record = eventService.acceptEvent(eventType, req.payload(), req.callbackUrl());
        return ResponseEntity.ok(Map.of("eventId", record.getEventId(), "message", "Event accepted for processing."));
    }

    @PostMapping("/ack")
    public ResponseEntity<String> acknowledge(@RequestBody Map<String, Object> payload) {
        log.info("Received callback payload: {}", payload);
        return ResponseEntity.ok("Acknowledged for processing!");
    }
}
