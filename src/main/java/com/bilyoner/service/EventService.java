package com.bilyoner.service;

import com.bilyoner.dto.CreateEventRequest;
import com.bilyoner.dto.EventDTO;
import com.bilyoner.mapper.EventMapper;
import com.bilyoner.model.Event;
import com.bilyoner.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final WebSocketNotificationService notificationService;
    private final Random random = new Random();

    public void createEvent(CreateEventRequest request) {
        Event event = eventRepository.save(eventMapper.createEventRequestToEvent(request));
        notificationService.notifyEventCreated(eventMapper.eventToEventDTO(event));
    }

    @Transactional(readOnly = true)
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::eventToEventDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Event findLatestEventByIdWithLock(Long eventId) {
        return eventRepository.findLatestEventByIdWithLock(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
    }

    @Transactional
    public void updateRandomOdds() {
        List<Event> events = eventRepository.findAllWithLocking();
        events.forEach(event -> {
            updateEventOdds(event);
            Event savedEvent = eventRepository.save(event);
            notificationService.notifyOddsUpdated(eventMapper.eventToEventDTO(savedEvent));
        });
    }

    private void updateEventOdds(Event event) {
        double variation = 0.05;
        event.setHomeWinOdds(adjustOdds(event.getHomeWinOdds(), variation));
        event.setDrawOdds(adjustOdds(event.getDrawOdds(), variation));
        event.setAwayWinOdds(adjustOdds(event.getAwayWinOdds(), variation));
        event.setUpdateDate(LocalDateTime.now());
    }

    private double adjustOdds(double currentOdds, double variation) {
        double change = (random.nextDouble() * 2 - 1) * variation;
        double newOdds = Math.max(1.01, currentOdds * (1 + change));

        BigDecimal bd = BigDecimal.valueOf(newOdds);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
} 