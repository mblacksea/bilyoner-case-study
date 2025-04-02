package com.bilyoner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "event_odds_history")
public class EventOddsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private Double homeWinOdds;

    @Column(nullable = false)
    private Double drawOdds;

    @Column(nullable = false)
    private Double awayWinOdds;

    @Column(nullable = false)
    private LocalDateTime createDate;
} 