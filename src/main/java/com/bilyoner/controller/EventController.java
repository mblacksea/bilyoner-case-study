package com.bilyoner.controller;

import com.bilyoner.dto.GeneralResponseDTO;
import com.bilyoner.dto.CreateEventRequest;
import com.bilyoner.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<GeneralResponseDTO> createEvent(@Valid @RequestBody CreateEventRequest request) {
        eventService.createEvent(request);
        return new ResponseEntity<>(
                new GeneralResponseDTO(HttpStatus.CREATED.value()),
                new HttpHeaders(),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GeneralResponseDTO> getAllEvents() {
        return new ResponseEntity<>(
                new GeneralResponseDTO(HttpStatus.OK.value(), eventService.getAllEvents()),
                new HttpHeaders(),
                HttpStatus.OK);
    }
} 