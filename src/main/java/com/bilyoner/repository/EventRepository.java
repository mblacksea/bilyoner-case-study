package com.bilyoner.repository;

import com.bilyoner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT e FROM Event e")
    List<Event> findAllWithLocking();
} 