package com.bilyoner.model;

import lombok.Getter;

@Getter
public enum BetType {
    HOME_WIN("HOME_WIN"),
    DRAW("DRAW"),
    AWAY_WIN("AWAY_WIN");

    private final String type;

    BetType(String type) {
        this.type = type;
    }
}
