package com.bilyoner.mapper;

import com.bilyoner.dto.CreateEventRequest;
import com.bilyoner.dto.EventDTO;
import com.bilyoner.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventMapperTest {

    @InjectMocks
    private EventMapper eventMapper;

    private CreateEventRequest createEventRequest;
    private Event event;

    @BeforeEach
    void setUp() {
        createEventRequest = new CreateEventRequest();
        createEventRequest.setLeagueName("Premier League");
        createEventRequest.setHomeTeam("Arsenal");
        createEventRequest.setAwayTeam("Chelsea");
        createEventRequest.setHomeWinOdds(2.12);
        createEventRequest.setDrawOdds(3.45);
        createEventRequest.setAwayWinOdds(3.23);
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
    }

    @Test
    void should_map_all_fields_correctly_when_converting_request_to_event() {
        Event result = eventMapper.createEventRequestToEvent(createEventRequest);

        assertNotNull(result);
        assertEquals("Premier League", result.getLeagueName());
        assertEquals("Arsenal", result.getHomeTeam());
        assertEquals("Chelsea", result.getAwayTeam());
        assertEquals(2.12, result.getHomeWinOdds());
        assertEquals(3.45, result.getDrawOdds());
        assertEquals(3.23, result.getAwayWinOdds());
        assertNotNull(result.getStartTime());
    }

    @Test
    void should_map_all_fields_correctly_when_converting_event_to_dto() {
        EventDTO result = eventMapper.eventToEventDTO(event);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Premier League", result.getLeagueName());
        assertEquals("Arsenal", result.getHomeTeam());
        assertEquals("Chelsea", result.getAwayTeam());
        assertEquals(2.10, result.getHomeWinOdds());
        assertEquals(3.40, result.getDrawOdds());
        assertEquals(3.20, result.getAwayWinOdds());
        assertEquals(event.getStartTime(), result.getStartTime());
    }


    @Test
    void should_throw_exception_when_create_request_is_null() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> eventMapper.createEventRequestToEvent(null));
        assertEquals("CreateEventRequest cannot be null", exception.getMessage());
    }

    @Test
    void should_handle_null_fields_when_creating_event_from_request() {
        CreateEventRequest request = new CreateEventRequest();
        request.setLeagueName("Test League");

        try {
            Event result = eventMapper.createEventRequestToEvent(request);
            
            assertNotNull(result);
            assertEquals("Test League", result.getLeagueName());
            assertNull(result.getHomeWinOdds());
            assertNull(result.getDrawOdds());
            assertNull(result.getAwayWinOdds());
        } catch (Exception e) {
            fail("Mapper should not throw exception for null fields: " + e.getMessage());
        }
    }

    @Test
    void should_throw_exception_when_event_is_null() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> eventMapper.eventToEventDTO(null));
        assertEquals("Event cannot be null", exception.getMessage());
    }
} 