package com.bilyoner.service;

import com.bilyoner.dto.CreateBetslipRequest;
import com.bilyoner.mapper.BetslipMapper;
import com.bilyoner.model.Betslip;
import com.bilyoner.model.Event;
import com.bilyoner.repository.BetslipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BetslipService {
    private final BetslipRepository betslipRepository;
    private final EventService eventService;
    private final BetslipMapper betslipMapper;

    @Value("${app.betting.max-bets-per-event}")
    private int maxBetsPerEvent;

    @Transactional
    public Betslip createBetslip(CreateBetslipRequest request, String customerId) {
        long existingBets = betslipRepository.countBetslipsByEventIdAndCustomerId(request.getEventId(), customerId);
        if (existingBets + request.getMultipleCount() > maxBetsPerEvent) {
            throw new IllegalStateException(
                String.format("Maximum bet limit (%d) exceeded for this event and customer", maxBetsPerEvent)
            );
        }

        Event event = eventService.getEventById(request.getEventId());
        Double currentOdds = switch (request.getSelectedBetType()) {
            case HOME_WIN -> event.getHomeWinOdds();
            case DRAW -> event.getDrawOdds();
            case AWAY_WIN -> event.getAwayWinOdds();
        };

        if (!currentOdds.equals(request.getExpectedOdds())) {
            throw new IllegalStateException(String.format(
                    "Odds have changed. Expected: %.2f, Current: %.2f",
                    request.getExpectedOdds(), currentOdds
            ));
        }

        Betslip betslip = betslipMapper.toBetslip(request, event, customerId);
        return betslipRepository.save(betslip);
    }
} 