package com.bilyoner.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssxxx", timezone = "UTC")
    private ZonedDateTime timestamp;

    private String message;
    private String debugMessage;

    private ApiError() {
        timestamp = ZonedDateTime.now();
    }

    public ApiError(String message) {
        this();
        this.message = message;
    }

}
