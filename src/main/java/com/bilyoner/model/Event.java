package com.bilyoner.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String leagueName;

    @Column(nullable = false)
    private String homeTeam;

    @Column(nullable = false)
    private String awayTeam;

    @Column(nullable = false)
    private Double homeWinOdds;

    @Column(nullable = false)
    private Double drawOdds;

    @Column(nullable = false)
    private Double awayWinOdds;

    @Column(nullable = false)
    private LocalDateTime startTime = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventOddsHistory> oddsHistory = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
    }

    @Version
    private Long version;

    public void updateOdds(Double homeWinOdds, Double drawOdds, Double awayWinOdds) {
        EventOddsHistory history = new EventOddsHistory();
        history.setEvent(this);
        history.setHomeWinOdds(this.homeWinOdds);
        history.setDrawOdds(this.drawOdds);
        history.setAwayWinOdds(this.awayWinOdds);
        history.setCreateDate(LocalDateTime.now());
        this.oddsHistory.add(history);

        this.homeWinOdds = homeWinOdds;
        this.drawOdds = drawOdds;
        this.awayWinOdds = awayWinOdds;
        this.updateDate = LocalDateTime.now();
    }

    public boolean validateOddsNotChanged(BetType betType, Double expectedOdds, LocalDateTime validationTime) {
        Double currentOdds = switch (betType) {
            case HOME_WIN -> this.homeWinOdds;
            case DRAW -> this.drawOdds;
            case AWAY_WIN -> this.awayWinOdds;
        };

        if (currentOdds.equals(expectedOdds)) {
            return true;
        }

        return oddsHistory.stream()
                .filter(history -> !history.getCreateDate().isAfter(validationTime))
                .max(Comparator.comparing(EventOddsHistory::getCreateDate))
                .map(history -> {
                    Double historicalOdds = switch (betType) {
                        case HOME_WIN -> history.getHomeWinOdds();
                        case DRAW -> history.getDrawOdds();
                        case AWAY_WIN -> history.getAwayWinOdds();
                    };
                    return historicalOdds.equals(expectedOdds);
                })
                .orElse(false);
    }
} 