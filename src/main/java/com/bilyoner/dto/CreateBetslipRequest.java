package com.bilyoner.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateBetslipRequest {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Max(value = 10000, message = "Maximum amount per betslip is 10000 TL")
    private BigDecimal amount;

    @NotNull(message = "Multiple count is required")
    @Min(value = 1, message = "Multiple count must be at least 1")
    @Max(value = 500, message = "Maximum multiple count is 500")
    private Integer multipleCount;

    @NotEmpty(message = "At least one bet is required")
    @Valid
    private List<BetRequestDTO> bets;
}
