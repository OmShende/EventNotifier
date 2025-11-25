package org.example.eventnotifier.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record EventRequest (
        @NotBlank String eventType,
        @NotNull Map<String, Object> payload,
        @NotBlank String callbackUrl
) {}
