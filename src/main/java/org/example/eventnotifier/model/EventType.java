package org.example.eventnotifier.model;

public enum EventType {
    EMAIL, SMS, PUSH;


    public static EventType from(String s) {
        return EventType.valueOf(s.toUpperCase());
    }
}
