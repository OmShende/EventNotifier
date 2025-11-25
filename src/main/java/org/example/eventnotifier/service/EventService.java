package org.example.eventnotifier.service;

import org.example.eventnotifier.model.EventRecord;
import org.example.eventnotifier.model.EventType;

import java.util.Map;

public interface EventService {
    EventRecord acceptEvent(EventType eventType, Map<String, Object> payload, String callbackUrl);
    void shutdown();
}
