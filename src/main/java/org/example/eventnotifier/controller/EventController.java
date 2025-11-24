package org.example.eventnotifier.controller;

import org.example.eventnotifier.model.EventRecord;
import org.example.eventnotifier.model.EventRequest;
import org.example.eventnotifier.model.EventType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @PostMapping
    public ResponseEntity<Map<String, String>> generateEvent(@RequestBody EventRequest req) {
//        EventType et;
//        try {
//            et = EventType.from(req.eventType());
//        } catch (IllegalArgumentException ex) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Invalid eventType"));
//        }
//
//
//        EventRecord record = eventService.acceptEvent(et, req.payload(), req.callbackUrl());
//        return ResponseEntity.ok(Map.of("eventId", record.getEventId(), "message", "Event accepted for processing."));
        return ResponseEntity.ok(Map.of());
    }
}
