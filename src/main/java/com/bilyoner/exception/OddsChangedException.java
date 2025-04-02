package com.bilyoner.exception;

import lombok.Getter;

@Getter
public class OddsChangedException extends RuntimeException {
    private final Long eventId;
    private final Double expectedOdds;
    private final Double currentOdds;

    public OddsChangedException(Long eventId, Double expectedOdds, Double currentOdds) {
        super(String.format("Odds have changed for event ID: %d. Expected: %.2f, Current: %.2f", 
              eventId, expectedOdds, currentOdds));
        this.eventId = eventId;
        this.expectedOdds = expectedOdds;
        this.currentOdds = currentOdds;
    }
} 