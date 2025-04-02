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
        Betslip betslip = betslipMapper.toBetslip(request, customerId);

        for (BetRequestDTO betRequest : request.getBets()) {
            Long existingBets = betslipRepository.countBetslipsByEventIdAndCustomerId(
                    betRequest.getEventId(), customerId);
            long totalExistingBets = existingBets != null ? existingBets : 0;

            if (totalExistingBets + request.getMultipleCount() > maxBetsPerEvent) {
                String totalExistingBetsMessage = String.format("Maximum bet limit (%d) exceeded for event ID: %d",
                        maxBetsPerEvent, betRequest.getEventId());
                throw new IllegalStateException(totalExistingBetsMessage);
            }

            Event event = eventService.findLatestEventByIdWithLock(betRequest.getEventId());
            Double currentOdds = switch (betRequest.getSelectedBetType()) {
                case HOME_WIN -> event.getHomeWinOdds();
                case DRAW -> event.getDrawOdds();
                case AWAY_WIN -> event.getAwayWinOdds();
            };

            if (!currentOdds.equals(betRequest.getExpectedOdds())) {
                log.warn("Odds have changed for event ID: {}. Expected: {}, Current: {}",
                        betRequest.getEventId(), betRequest.getExpectedOdds(), currentOdds);
                throw new OddsChangedException(betRequest.getEventId(), betRequest.getExpectedOdds(), currentOdds);
            }

            Bet bet = betslipMapper.toBet(betRequest, event, betslip);
            betslip.getBets().add(bet);
        }

        return betslipRepository.save(betslip);
    }
} 