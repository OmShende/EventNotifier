package org.example.eventnotifier.processor;

import org.example.eventnotifier.util.CallbackClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorsConfig {

    @Bean(name = "emailProcessor")
    public EventProcessor emailProcessor(
            @Value("${app.queues.email-capacity}") int capacity,
            @Value("${app.simulateFailureRate}") double failureRate,
            @Value("${app.processingTime.email}") long processingTime,
            CallbackClient callbackClient
    ) {
        return new AbstractEventProcessor("email", capacity, processingTime, failureRate, callbackClient) {};
    }

    @Bean(name = "smsProcessor")
    public EventProcessor smsProcessor(
            @Value("${app.queues.sms-capacity}") int capacity,
            @Value("${app.simulateFailureRate}") double failureRate,
            @Value("${app.processingTime.sms}") long processingTime,
            CallbackClient callbackClient
    ) {
        return new AbstractEventProcessor("sms", capacity, processingTime, failureRate, callbackClient) {};
    }

    @Bean(name = "pushProcessor")
    public EventProcessor pushProcessor(
            @Value("${app.queues.push-capacity}") int capacity,
            @Value("${app.simulateFailureRate}") double failureRate,
            @Value("${app.processingTime.push}") long processingTime,
            CallbackClient callbackClient
    ) {
        return new AbstractEventProcessor("push", capacity, processingTime, failureRate, callbackClient) {};
    }
}
