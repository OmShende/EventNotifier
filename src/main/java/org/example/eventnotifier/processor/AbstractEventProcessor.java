package org.example.eventnotifier.processor;

import lombok.extern.slf4j.Slf4j;
import org.example.eventnotifier.model.EventRecord;
import org.example.eventnotifier.util.CallbackClient;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class AbstractEventProcessor implements EventProcessor, Runnable {
    private final BlockingQueue<EventRecord> queue;
    private final Thread worker;
    private final long processingMillis;
    private final double failureRate;
    private final CallbackClient callbackClient;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Random random = new Random();

    protected AbstractEventProcessor(
            String name,
            int capacity,
            long processingMillis,
            double failureRate,
            CallbackClient callbackClient
    ) {
        this.queue = new LinkedBlockingQueue<>(capacity);
        this.processingMillis = processingMillis;
        this.failureRate = failureRate;
        this.callbackClient = callbackClient;
        this.worker = new Thread(this, name + "-worker");
        this.worker.start();
    }

    @Override
    public void enqueue(EventRecord record) {
        try {
            queue.put(record);
            log.info("Enqueued {} to {}", record.getEventId(), worker.getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (running.get() || !queue.isEmpty()) {
            try {
                EventRecord rec = queue.poll();
                if (rec == null) {
                    Thread.sleep(100);
                    continue;
                }
                processWithDelay(rec);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("{} exiting gracefully", worker.getName());
    }

    private void processWithDelay(EventRecord rec) throws InterruptedException {
        log.info("Processing {} (type={})", rec.getEventId(), rec.getEventType());
        Thread.sleep(processingMillis);

        boolean failed = random.nextDouble() < failureRate;

        if (failed) {
            log.warn("Processing failed for {}", rec.getEventId());
            callbackClient.sendFailure(rec, "Simulated processing failure", Instant.now());
        } else {
            callbackClient.sendSuccess(rec, Instant.now());
        }
    }

    @Override
    public void stop() {
        running.set(false);
        worker.interrupt();

        try {
            worker.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
