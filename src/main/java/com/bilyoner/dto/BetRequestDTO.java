package com.bilyoner.dto;

import com.bilyoner.model.BetType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BetRequestDTO {
    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Selected bet type is required")
    private BetType selectedBetType;

    @NotNull(message = "Expected odds is required")
    @Positive(message = "Expected odds must be positive")
    private Double expectedOdds;
} 