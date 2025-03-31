package com.bilyoner.controller;

import com.bilyoner.dto.CreateBetslipRequest;
import com.bilyoner.model.Betslip;
import com.bilyoner.service.BetslipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/betslips")
@RequiredArgsConstructor
public class BetslipController {
    private final BetslipService betslipService;

    @PostMapping
    public ResponseEntity<Betslip> createBetslip(
            @Valid @RequestBody CreateBetslipRequest request,
            @RequestHeader("X-Customer-Id") String customerId) {
        return ResponseEntity.ok(betslipService.createBetslip(request, customerId));
    }
} 