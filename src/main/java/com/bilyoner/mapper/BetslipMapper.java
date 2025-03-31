package com.bilyoner.mapper;

import com.bilyoner.dto.CreateBetslipRequest;
import com.bilyoner.model.Betslip;
import com.bilyoner.model.Event;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BetslipMapper {

    public Betslip toBetslip(CreateBetslipRequest request, Event event, String customerId) {
        Betslip betslip = new Betslip();
        betslip.setCustomerId(customerId);
        betslip.setEvent(event);
        betslip.setSelectedBetType(request.getSelectedBetType());
        betslip.setAmount(request.getAmount());
        betslip.setMultipleCount(request.getMultipleCount());
        
        Double odds = switch (request.getSelectedBetType()) {
            case HOME_WIN -> event.getHomeWinOdds();
            case DRAW -> event.getDrawOdds();
            case AWAY_WIN -> event.getAwayWinOdds();
        };
        
        betslip.setOdds(roundToTwoDecimals(odds));
        return betslip;
    }

    private Double roundToTwoDecimals(Double value) {
        if (value == null) return null;
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
