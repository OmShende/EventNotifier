package org.example.eventnotifier.processor;

import org.example.eventnotifier.model.EventRecord;

public interface EventProcessor {
    void enqueue(EventRecord eventRecord);
    void stop();
}