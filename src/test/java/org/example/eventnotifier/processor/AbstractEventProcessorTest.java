package org.example.eventnotifier.processor;

import org.example.eventnotifier.model.EventRecord;
import org.example.eventnotifier.model.EventType;
import org.example.eventnotifier.util.CallbackClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Map;

import static org.mockito.Mockito.*;

class AbstractEventProcessorTest {

    private final CallbackClient callbackClient = Mockito.mock(CallbackClient.class);
    private final AbstractEventProcessor processor = new AbstractEventProcessor("fifo-test", 10, 40L, 0.0, callbackClient) {};

    @AfterEach
    void tearDown() {
        processor.stop();
    }

    @Test
    void eventsAreProcessedInFifoOrder() throws InterruptedException {
        EventRecord r1 = new EventRecord(EventType.EMAIL, Map.of("recipient", "a@b.com"), "http://localhost/cb");
        EventRecord r2 = new EventRecord(EventType.EMAIL, Map.of("recipient", "c@d.com"), "http://localhost/cb");
        EventRecord r3 = new EventRecord(EventType.EMAIL, Map.of("recipient", "e@f.com"), "http://localhost/cb");

        processor.enqueue(r1);
        processor.enqueue(r2);
        processor.enqueue(r3);

        // wait enough time for all three to be processed sequentially
        Thread.sleep(500);

        InOrder inOrder = inOrder(callbackClient);
        inOrder.verify(callbackClient).sendSuccess(eq(r1), any());
        inOrder.verify(callbackClient).sendSuccess(eq(r2), any());
        inOrder.verify(callbackClient).sendSuccess(eq(r3), any());
    }

    @Test
    void shouldInvokeSendFailure_whenProcessingAlwaysFails() throws InterruptedException {
        EventRecord r = new EventRecord(EventType.EMAIL, Map.of("recipient", "a@b.com"), "http://localhost/cb");
        AbstractEventProcessor processor = new AbstractEventProcessor("fail-test", 10, 30L, 1.0, callbackClient) {};

        processor.enqueue(r);

        Thread.sleep(150);

        verify(callbackClient, atLeastOnce()).sendFailure(eq(r), anyString(), any());
        verify(callbackClient, never()).sendSuccess(eq(r), any());
    }
}
