package com.bilyoner.repository;

import com.bilyoner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT e FROM Event e")
    List<Event> findAllWithLocking();

    @Query("SELECT e FROM Event e JOIN FETCH e.oddsHistory WHERE e.id = :eventId")
    Optional<Event> findByIdWithOddsHistory(@Param("eventId") Long eventId);
} 