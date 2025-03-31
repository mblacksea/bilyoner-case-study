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
        Betslip betslip = new Betslip();
        betslip.setCustomerId(customerId);
        betslip.setAmount(request.getAmount());
        betslip.setMultipleCount(request.getMultipleCount());
        betslip.setBets(new ArrayList<>());
        return betslip;
    }

    public Bet toBet(BetRequestDTO request, Event event, Betslip betslip) {
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
