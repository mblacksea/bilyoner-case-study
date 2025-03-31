package com.bilyoner.controller;

import com.bilyoner.dto.GeneralResponseDTO;
import com.bilyoner.dto.EventDTO;
import com.bilyoner.dto.CreateEventRequest;
import com.bilyoner.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event Management", description = "Endpoints for managing betting events")
public class EventController {
    private final EventService eventService;

    @Operation(summary = "Create a new event", 
              description = "Creates a new betting event with the specified details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Event created successfully",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<GeneralResponseDTO> createEvent(
            @Parameter(description = "Event creation request details")
            @Valid @RequestBody CreateEventRequest request) {
        eventService.createEvent(request);
        return new ResponseEntity<>(
                new GeneralResponseDTO(HttpStatus.CREATED.value()),
                new HttpHeaders(),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get all events", description = "Retrieves a list of all available betting events")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Events found",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class))),
        @ApiResponse(responseCode = "204", description = "No events available",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<GeneralResponseDTO> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        if (events.isEmpty()) {
            return new ResponseEntity<>(
                new GeneralResponseDTO(HttpStatus.NO_CONTENT.value()),
                new HttpHeaders(),
                HttpStatus.NO_CONTENT
            );
        }
        return new ResponseEntity<>(
            new GeneralResponseDTO(HttpStatus.OK.value(), events),
            new HttpHeaders(),
            HttpStatus.OK
        );
    }
} 