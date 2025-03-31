package com.bilyoner.repository;

import com.bilyoner.model.Betslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BetslipRepository extends JpaRepository<Betslip, Long> {
    @Query("SELECT SUM(bs.multipleCount) FROM Betslip bs JOIN bs.bets b WHERE b.event.id = :eventId AND bs.customerId = :customerId")
    Long countBetslipsByEventIdAndCustomerId(@Param("eventId") Long eventId, @Param("customerId") String customerId);
} 