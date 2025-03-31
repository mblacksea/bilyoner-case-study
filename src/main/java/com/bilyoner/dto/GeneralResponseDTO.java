package com.bilyoner.dto;

import com.bilyoner.exception.ApiError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponseDTO {
    private int statusCode;
    private Object body;
    private ApiError apiError;

    public GeneralResponseDTO(int statusCode) {
        this.statusCode = statusCode;
    }

    public GeneralResponseDTO(int statusCode, Object body) {
        this.statusCode = statusCode;
        this.body = body;
    }

}
