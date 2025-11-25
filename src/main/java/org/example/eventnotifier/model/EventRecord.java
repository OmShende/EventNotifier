package org.example.eventnotifier.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class EventRecord {
    private final String eventId;
    private final EventType eventType;
    private final Map<String, Object> payload;
    private final String callbackUrl;
    private final Instant createdAt;


    public EventRecord(EventType eventType, Map<String, Object> payload, String callbackUrl) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.payload = payload;
        this.callbackUrl = callbackUrl;
        this.createdAt = Instant.now();
    }
}
