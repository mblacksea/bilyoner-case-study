package com.bilyoner.config;

import com.bilyoner.service.CleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CleanupConfig {
    private final CleanupService cleanupService;

    @Scheduled(cron = "${app.cleanup.odds-history-cron}")
    public void cleanupOddsHistory() {
        cleanupService.cleanupOddsHistory();
    }
} 