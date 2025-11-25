package org.example.eventnotifier.service;

import org.example.eventnotifier.model.EventRecord;
import org.example.eventnotifier.model.EventType;
import org.example.eventnotifier.processor.EventProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.mockito.Mockito.verify;

class EventHandlerTest {

    @Test
    void acceptEvent_routesToCorrectProcessor() {
        EventProcessor emailProc = Mockito.mock(EventProcessor.class);
        EventProcessor smsProc = Mockito.mock(EventProcessor.class);
        EventProcessor pushProc = Mockito.mock(EventProcessor.class);

        EventHandler service = new EventHandler(emailProc, smsProc, pushProc);

        EventRecord recEmail = service.acceptEvent(EventType.EMAIL, Map.of("recipient", "a@b.com"), "http://cb");
        verify(emailProc).enqueue(Mockito.eq(recEmail));

        EventRecord recSms = service.acceptEvent(EventType.SMS, Map.of("phoneNumber", "+9112345"), "http://cb");
        verify(smsProc).enqueue(Mockito.eq(recSms));

        EventRecord recPush = service.acceptEvent(EventType.PUSH, Map.of("deviceId", "dev1"), "http://cb");
        verify(pushProc).enqueue(Mockito.eq(recPush));
    }
}