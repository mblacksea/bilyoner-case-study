package com.bilyoner.service;

import com.bilyoner.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    private static final String EVENT_TOPIC = "/topic/events";

    public void notifyEventCreated(EventDTO eventDTO) {
        messagingTemplate.convertAndSend(EVENT_TOPIC, eventDTO);
    }

    public void notifyOddsUpdated(EventDTO eventDTO) {
        messagingTemplate.convertAndSend(EVENT_TOPIC, eventDTO);
    }
} 