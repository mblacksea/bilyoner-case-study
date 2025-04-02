package com.bilyoner.exception;

import com.bilyoner.dto.GeneralResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        var apiError = new ApiError(ex.getMessage());
        return buildResponseEntity(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("Unhandled error in controller advice.", ex);
        var apiError = new ApiError(ex.getMessage());
        return buildResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<GeneralResponseDTO> handleIllegalStateException(IllegalStateException ex) {
        GeneralResponseDTO response = new GeneralResponseDTO(
            HttpStatus.CONFLICT.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OddsChangedException.class)
    public ResponseEntity<GeneralResponseDTO> handleOddsChangedException(OddsChangedException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put("eventId", ex.getEventId());
        details.put("expectedOdds", ex.getExpectedOdds());
        details.put("currentOdds", ex.getCurrentOdds());
        
        GeneralResponseDTO response = new GeneralResponseDTO(
            HttpStatus.CONFLICT.value(),
            "Odds have changed. Please check the current odds and try again.",
            details
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus httpStatus) {
        return new ResponseEntity<>(
                new GeneralResponseDTO(httpStatus.value(), null, apiError),
                httpStatus);
    }
}
