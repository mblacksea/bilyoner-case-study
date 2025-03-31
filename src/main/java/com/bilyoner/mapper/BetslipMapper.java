package com.bilyoner.mapper;

import com.bilyoner.dto.CreateBetslipRequest;
import com.bilyoner.dto.BetRequestDTO;
import com.bilyoner.model.Bet;
import com.bilyoner.model.Betslip;
import com.bilyoner.model.Event;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BetslipMapper {

    public Betslip toBetslip(CreateBetslipRequest request, String customerId) {
        if (request == null) {
            throw new IllegalArgumentException("CreateBetslipRequest cannot be null");
        }
        
        Betslip betslip = new Betslip();
        betslip.setCustomerId(customerId);
        betslip.setAmount(request.getAmount());
        betslip.setMultipleCount(request.getMultipleCount());
        betslip.setBets(new ArrayList<>());
        return betslip;
    }

    public Bet toBet(BetRequestDTO request, Event event, Betslip betslip) {
        if (request == null) {
            throw new IllegalArgumentException("BetRequestDTO cannot be null");
        }
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        if (betslip == null) {
            throw new IllegalArgumentException("Betslip cannot be null");
        }
        
        Bet bet = new Bet();
        bet.setBetslip(betslip);
        bet.setEvent(event);
        bet.setSelectedBetType(request.getSelectedBetType());

        Double odds = switch (request.getSelectedBetType()) {
            case HOME_WIN -> event.getHomeWinOdds();
            case DRAW -> event.getDrawOdds();
            case AWAY_WIN -> event.getAwayWinOdds();
        };

        bet.setOdds(odds);
        return bet;
    }

}
