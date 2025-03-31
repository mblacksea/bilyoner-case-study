package com.bilyoner.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDTO {

    private Long id;
    private String leagueName;
    private String homeTeam;
    private String awayTeam;
    private Double homeWinOdds;
    private Double drawOdds;
    private Double awayWinOdds;
    private LocalDateTime startTime;
}
