package com.bilyoner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponseDTO {
    private int status;
    private String message;
    private Object data;
    private Object error;
    private Map<String, Object> details;

    public GeneralResponseDTO(int status) {
        this.status = status;
    }

    public GeneralResponseDTO(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    public GeneralResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public GeneralResponseDTO(int status, Object data, Object error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public GeneralResponseDTO(int status, String message, Map<String, Object> details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }
}
