package com.bilyoner.repository;

import com.bilyoner.model.EventOddsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventOddsHistoryRepository extends JpaRepository<EventOddsHistory, Long> {
    
    @Modifying
    @Query("DELETE FROM EventOddsHistory eoh WHERE eoh.createDate < :cutoffDate")
    int deleteByCreateDateBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
} 