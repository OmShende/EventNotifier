package org.example.eventnotifier.util;

import lombok.extern.slf4j.Slf4j;
import org.example.eventnotifier.model.EventRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
public class CallbackClient {
    private final WebClient webClient;

    public CallbackClient() {
        this.webClient = WebClient.builder().build();
    }

    public void sendSuccess(EventRecord record, Instant processedAt) {
        Map<String, Object> body = Map.of(
                "eventId", record.getEventId(),
                "status", "COMPLETED",
                "eventType", record.getEventType().name(),
                "processedAt", processedAt.toString()
        );
        post(record.getCallbackUrl(), body);
    }

    public void sendFailure(EventRecord record, String errorMessage, Instant processedAt) {
        Map<String, Object> body = Map.of(
                "eventId", record.getEventId(),
                "status", "FAILED",
                "eventType", record.getEventType().name(),
                "errorMessage", errorMessage,
                "processedAt", processedAt.toString()
        );
        post(record.getCallbackUrl(), body);
    }

    private void post(String url, Map<String, Object> body) {
        try {
            webClient.post()
                    .uri(url)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("Callback sent to {}", url);
        } catch (Exception e) {
            log.error("Callback to {} failed: {}", url, e.getMessage());
        }
    }
}
