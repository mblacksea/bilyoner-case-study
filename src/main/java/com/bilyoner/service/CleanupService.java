package com.bilyoner.service;

import com.bilyoner.repository.EventOddsHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanupService {
    private final EventOddsHistoryRepository eventOddsHistoryRepository;

    @Value("${app.cleanup.odds-history-retention-minutes}")
    private int retentionMinutes;

    @Transactional
    public void cleanupOddsHistory() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusMinutes(retentionMinutes);
        
        try {
            int deletedCount = eventOddsHistoryRepository.deleteByCreateDateBefore(cutoffDate);
            if (deletedCount > 0) {
                log.info("Cleaned up {} odds history records older than {} minutes", 
                    deletedCount, retentionMinutes);
            }
        } catch (Exception e) {
            log.error("Error while cleaning up odds history", e);
        }
    }
} 