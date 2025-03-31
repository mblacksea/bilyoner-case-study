package com.bilyoner.controller;

import com.bilyoner.dto.CreateBetslipRequest;
import com.bilyoner.dto.GeneralResponseDTO;
import com.bilyoner.model.Betslip;
import com.bilyoner.service.BetslipService;
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

@RestController
@RequestMapping("/api/betslips")
@RequiredArgsConstructor
@Tag(name = "Betslip Management", description = "Endpoints for managing betting slips")
public class BetslipController {

    private final BetslipService betslipService;

    @Operation(summary = "Create a new betslip", 
              description = "Creates a new betslip with the specified bets and customer information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Betslip created successfully",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Business rule violation (e.g., odds changed, limit exceeded)",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<GeneralResponseDTO> createBetslip(
            @Parameter(description = "Betslip creation request details") 
            @Valid @RequestBody CreateBetslipRequest request,
            @Parameter(description = "Customer ID") 
            @RequestHeader("X-Customer-Id") String customerId) {
        Betslip betslip = betslipService.createBetslip(request, customerId);
        return new ResponseEntity<>(
                new GeneralResponseDTO(HttpStatus.CREATED.value(), betslip),
                new HttpHeaders(),
                HttpStatus.CREATED);
    }
} 