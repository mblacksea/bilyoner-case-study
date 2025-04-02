package com.bilyoner.service;

import com.bilyoner.dto.CreateEventRequest;
import com.bilyoner.dto.EventDTO;
import com.bilyoner.mapper.EventMapper;
import com.bilyoner.model.Event;
import com.bilyoner.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private WebSocketNotificationService notificationService;

    @InjectMocks
    private EventService eventService;

    private CreateEventRequest createEventRequest;
    private Event event;
    private EventDTO eventDTO;

    @BeforeEach
    void setUp() {
        createEventRequest = new CreateEventRequest();
        createEventRequest.setLeagueName("Premier League");
        createEventRequest.setHomeTeam("Arsenal");
        createEventRequest.setAwayTeam("Chelsea");
        createEventRequest.setHomeWinOdds(2.10);
        createEventRequest.setDrawOdds(3.40);
        createEventRequest.setAwayWinOdds(3.20);
        createEventRequest.setStartTime(LocalDateTime.now());

        event = new Event();
        event.setId(1L);
        event.setLeagueName("Premier League");
        event.setHomeTeam("Arsenal");
        event.setAwayTeam("Chelsea");
        event.setHomeWinOdds(2.10);
        event.setDrawOdds(3.40);
        event.setAwayWinOdds(3.20);
        event.setStartTime(LocalDateTime.now());
        event.setVersion(0L);

        eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setLeagueName("Premier League");
        eventDTO.setHomeTeam("Arsenal");
        eventDTO.setAwayTeam("Chelsea");
        eventDTO.setHomeWinOdds(2.10);
        eventDTO.setDrawOdds(3.40);
        eventDTO.setAwayWinOdds(3.20);
        eventDTO.setStartTime(LocalDateTime.now());
    }

    @Test
    void should_create_event_and_notify_when_valid_request_provided() {
        when(eventMapper.createEventRequestToEvent(any(CreateEventRequest.class))).thenReturn(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.eventToEventDTO(any(Event.class))).thenReturn(eventDTO);
        doNothing().when(notificationService).notifyEventCreated(any(EventDTO.class));

        eventService.createEvent(createEventRequest);

        verify(eventMapper, times(1)).createEventRequestToEvent(eq(createEventRequest));
        verify(eventRepository, times(1)).save(eq(event));
        verify(eventMapper, times(1)).eventToEventDTO(eq(event));
        verify(notificationService, times(1)).notifyEventCreated(eq(eventDTO));
    }

    @Test
    void should_return_all_events_as_dtos_when_requested() {
        List<Event> events = new ArrayList<>();
        events.add(event);
        
        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.eventToEventDTO(any(Event.class))).thenReturn(eventDTO);

        List<EventDTO> result = eventService.getAllEvents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventDTO, result.get(0));
        
        verify(eventRepository, times(1)).findAll();
        verify(eventMapper, times(1)).eventToEventDTO(eq(event));
    }

    @Test
    void should_return_event_when_found_by_id() {
        when(eventRepository.findLatestEventByIdWithLock(anyLong())).thenReturn(Optional.of(event));

        Event result = eventService.findLatestEventByIdWithLock(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Premier League", result.getLeagueName());
        assertEquals("Arsenal", result.getHomeTeam());
        assertEquals("Chelsea", result.getAwayTeam());
        
        verify(eventRepository, times(1)).findLatestEventByIdWithLock(eq(1L));
    }

    @Test
    void should_throw_exception_when_event_not_found() {
        when(eventRepository.findLatestEventByIdWithLock(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            eventService.findLatestEventByIdWithLock(1L);
        });
        
        verify(eventRepository, times(1)).findLatestEventByIdWithLock(1L);
    }

    @Test
    void should_update_odds_and_notify_clients_when_scheduled() {
        List<Event> events = new ArrayList<>();
        events.add(event);
        
        when(eventRepository.findAllWithLocking()).thenReturn(events);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.eventToEventDTO(any(Event.class))).thenReturn(eventDTO);
        doNothing().when(notificationService).notifyOddsUpdated(any(EventDTO.class));

        eventService.updateRandomOdds();

        verify(eventRepository, times(1)).findAllWithLocking();
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(eventMapper, times(1)).eventToEventDTO(any(Event.class));
        verify(notificationService, times(1)).notifyOddsUpdated(eq(eventDTO));
        
        assertTrue(event.getHomeWinOdds() != 2.10 || event.getDrawOdds() != 3.40 || event.getAwayWinOdds() != 3.20);
    }
} 