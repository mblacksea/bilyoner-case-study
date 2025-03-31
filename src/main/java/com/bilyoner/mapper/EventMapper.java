package com.bilyoner.mapper;

import com.bilyoner.dto.CreateEventRequest;
import com.bilyoner.dto.EventDTO;
import com.bilyoner.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event createEventRequestToEvent(CreateEventRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("CreateEventRequest cannot be null");
        }

        Event event = new Event();
        event.setLeagueName(request.getLeagueName());
        event.setHomeTeam(request.getHomeTeam());
        event.setAwayTeam(request.getAwayTeam());
        event.setHomeWinOdds(request.getHomeWinOdds());
        event.setDrawOdds(request.getDrawOdds());
        event.setAwayWinOdds(request.getAwayWinOdds());
        event.setStartTime(request.getStartTime());
        return event;
    }

    public EventDTO eventToEventDTO(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setLeagueName(event.getLeagueName());
        dto.setHomeTeam(event.getHomeTeam());
        dto.setAwayTeam(event.getAwayTeam());
        dto.setHomeWinOdds(event.getHomeWinOdds());
        dto.setDrawOdds(event.getDrawOdds());
        dto.setAwayWinOdds(event.getAwayWinOdds());
        dto.setStartTime(event.getStartTime());
        return dto;
    }

}
