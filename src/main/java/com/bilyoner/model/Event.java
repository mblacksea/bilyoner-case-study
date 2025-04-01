package com.bilyoner.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
    }

    @Version
    private Long version;
} 