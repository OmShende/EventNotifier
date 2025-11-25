package org.example.eventnotifier.service;

import org.example.eventnotifier.model.EventRecord;
import org.example.eventnotifier.model.EventType;
import org.example.eventnotifier.processor.EventProcessor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventHandler implements EventService, DisposableBean {
    private final EventProcessor emailProcessor;
    private final EventProcessor smsProcessor;
    private final EventProcessor pushProcessor;


    public EventHandler(EventProcessor emailProcessor,
                        EventProcessor smsProcessor,
                        EventProcessor pushProcessor) {
        this.emailProcessor = emailProcessor;
        this.smsProcessor = smsProcessor;
        this.pushProcessor = pushProcessor;
    }


    @Override
    public EventRecord acceptEvent(EventType eventType, Map<String, Object> payload, String callbackUrl) {
        EventRecord record = new EventRecord(eventType, payload, callbackUrl);
        switch (eventType) {
            case EMAIL -> emailProcessor.enqueue(record);
            case SMS -> smsProcessor.enqueue(record);
            case PUSH -> pushProcessor.enqueue(record);
            default -> throw new IllegalArgumentException("Unsupported event type: " + eventType);
        }
        return record;
    }


    @Override
    public void destroy() {
        shutdown();
    }


    @Override
    public void shutdown() {
        emailProcessor.stop();
        smsProcessor.stop();
        pushProcessor.stop();
    }
}
