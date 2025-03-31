package com.bilyoner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateEventRequest {
    @NotBlank(message = "League name is required")
    private String leagueName;

    @NotBlank(message = "Home team name is required")
    private String homeTeam;

    @NotBlank(message = "Away team name is required")
    private String awayTeam;

    @NotNull(message = "Home win odds is required")
    @Positive(message = "Home win odds must be positive")
    private Double homeWinOdds;

    @NotNull(message = "Draw odds is required")
    @Positive(message = "Draw odds must be positive")
    private Double drawOdds;

    @NotNull(message = "Away win odds is required")
    @Positive(message = "Away win odds must be positive")
    private Double awayWinOdds;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime = LocalDateTime.now();

} 