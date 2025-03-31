package com.bilyoner.dto;

import com.bilyoner.model.BetType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateBetslipRequest {
    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Selected bet type is required")
    private BetType selectedBetType;

    @NotNull(message = "Expected odds is required")
    @Positive(message = "Expected odds must be positive")
    private Double expectedOdds;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Max(value = 10000, message = "Maximum amount per betslip is 10000 TL")
    private BigDecimal amount;

    @NotNull(message = "Multiple count is required")
    @Min(value = 1, message = "Multiple count must be at least 1")
    @Max(value = 500, message = "Maximum multiple count is 500")
    private Integer multipleCount;
} 