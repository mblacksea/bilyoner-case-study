package com.bilyoner.service;

import com.bilyoner.dto.CreateBetslipRequest;
import com.bilyoner.dto.BetRequestDTO;
import com.bilyoner.exception.OddsChangedException;
import com.bilyoner.mapper.BetslipMapper;
import com.bilyoner.model.Bet;
import com.bilyoner.model.Betslip;
import com.bilyoner.model.Event;
import com.bilyoner.repository.BetslipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetslipService {
    private final BetslipRepository betslipRepository;
    private final EventService eventService;
    private final BetslipMapper betslipMapper;

    @Value("${app.betting.max-bets-per-event}")
    private int maxBetsPerEvent;

    @Transactional(timeout = 2)
    public Betslip createBetslip(CreateBetslipRequest request, String customerId) {
        LocalDateTime validationTime = LocalDateTime.now();
        Betslip betslip = betslipMapper.toBetslip(request, customerId);

        for (BetRequestDTO betRequest : request.getBets()) {
            validateBetLimit(betRequest.getEventId(), customerId, request.getMultipleCount());
            
            Event event = eventService.findEventById(betRequest.getEventId());
            validateOdds(betRequest, event, validationTime);

            Bet bet = betslipMapper.toBet(betRequest, event, betslip);
            betslip.getBets().add(bet);
        }

        return betslipRepository.save(betslip);
    }

    private void validateBetLimit(Long eventId, String customerId, Integer multipleCount) {
        Long existingBets = betslipRepository.countBetslipsByEventIdAndCustomerId(eventId, customerId);
        long totalExistingBets = existingBets != null ? existingBets : 0;

        if (totalExistingBets + multipleCount > maxBetsPerEvent) {
            String message = String.format("Maximum bet limit (%d) exceeded for event ID: %d",
                    maxBetsPerEvent, eventId);
            throw new IllegalStateException(message);
        }
    }

    private void validateOdds(BetRequestDTO betRequest, Event event, LocalDateTime validationTime) {
        if (!event.validateOddsNotChanged(betRequest.getSelectedBetType(), betRequest.getExpectedOdds(), validationTime)) {
            Double currentOdds = switch (betRequest.getSelectedBetType()) {
                case HOME_WIN -> event.getHomeWinOdds();
                case DRAW -> event.getDrawOdds();
                case AWAY_WIN -> event.getAwayWinOdds();
            };
            
            log.warn("Odds have changed for event ID: {}. Expected: {}, Current: {}",
                    betRequest.getEventId(), betRequest.getExpectedOdds(), currentOdds);
            throw new OddsChangedException(betRequest.getEventId(), betRequest.getExpectedOdds(), currentOdds);
        }
    }
} 