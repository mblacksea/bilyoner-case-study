package com.bilyoner.config;

import com.bilyoner.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {
    private final EventService eventService;

    @Scheduled(fixedRateString = "${app.scheduling.odds-update-interval}")
    public void updateOdds() {
        eventService.updateRandomOdds();
    }
} 