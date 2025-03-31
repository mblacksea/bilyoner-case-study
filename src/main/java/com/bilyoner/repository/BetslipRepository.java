package com.bilyoner.repository;

import com.bilyoner.model.Betslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BetslipRepository extends JpaRepository<Betslip, Long> {
    @Query("SELECT COUNT(b) FROM Betslip b WHERE b.event.id = :eventId AND b.customerId = :customerId")
    long countBetslipsByEventIdAndCustomerId(@Param("eventId") Long eventId, @Param("customerId") String customerId);
} 