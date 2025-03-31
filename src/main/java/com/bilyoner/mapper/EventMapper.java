package com.bilyoner.mapper;

import com.bilyoner.dto.CreateEventRequest;
import com.bilyoner.dto.EventDTO;
import com.bilyoner.model.Event;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class EventMapper {

    public Event createEventRequestToEvent(CreateEventRequest request) {
        Event event = new Event();
        event.setLeagueName(request.getLeagueName());
        event.setHomeTeam(request.getHomeTeam());
        event.setAwayTeam(request.getAwayTeam());
        event.setHomeWinOdds(roundToTwoDecimals(request.getHomeWinOdds()));
        event.setDrawOdds(roundToTwoDecimals(request.getDrawOdds()));
        event.setAwayWinOdds(roundToTwoDecimals(request.getAwayWinOdds()));
        event.setStartTime(request.getStartTime());
        return event;
    }
    
    public EventDTO eventToEventDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setLeagueName(event.getLeagueName());
        dto.setHomeTeam(event.getHomeTeam());
        dto.setAwayTeam(event.getAwayTeam());
        dto.setHomeWinOdds(roundToTwoDecimals(event.getHomeWinOdds()));
        dto.setDrawOdds(roundToTwoDecimals(event.getDrawOdds()));
        dto.setAwayWinOdds(roundToTwoDecimals(event.getAwayWinOdds()));
        dto.setStartTime(event.getStartTime());
        return dto;
    }

    private Double roundToTwoDecimals(Double value) {
        if (value == null) return null;
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
