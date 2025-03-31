package com.bilyoner.mapper;

import com.bilyoner.dto.BetRequestDTO;
import com.bilyoner.dto.CreateBetslipRequest;
import com.bilyoner.model.Bet;
import com.bilyoner.model.BetType;
import com.bilyoner.model.Betslip;
import com.bilyoner.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BetslipMapperTest {

    @InjectMocks
    private BetslipMapper betslipMapper;

    private CreateBetslipRequest createBetslipRequest;
    private BetRequestDTO betRequestDTO;
    private Event event;
    private Betslip betslip;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setLeagueName("Premier League");
        event.setHomeTeam("Arsenal");
        event.setAwayTeam("Chelsea");
        event.setHomeWinOdds(2.10);
        event.setDrawOdds(3.40);
        event.setAwayWinOdds(3.20);
        event.setStartTime(LocalDateTime.now());

        betRequestDTO = new BetRequestDTO();
        betRequestDTO.setEventId(1L);
        betRequestDTO.setSelectedBetType(BetType.HOME_WIN);
        betRequestDTO.setExpectedOdds(2.10);

        List<BetRequestDTO> bets = new ArrayList<>();
        bets.add(betRequestDTO);

        createBetslipRequest = new CreateBetslipRequest();
        createBetslipRequest.setAmount(new BigDecimal("100"));
        createBetslipRequest.setMultipleCount(2);
        createBetslipRequest.setBets(bets);

        betslip = new Betslip();
        betslip.setId(1L);
        betslip.setCustomerId("customer123");
        betslip.setAmount(new BigDecimal("100"));
        betslip.setMultipleCount(2);
        betslip.setBets(new ArrayList<>());
    }

    @Test
    void should_return_valid_betslip_when_given_valid_request() {
        Betslip result = betslipMapper.toBetslip(createBetslipRequest, "customer123");

        assertNotNull(result);
        assertEquals("customer123", result.getCustomerId());
        assertEquals(new BigDecimal("100"), result.getAmount());
        assertEquals(2, result.getMultipleCount());
        assertNotNull(result.getBets());
        assertTrue(result.getBets().isEmpty());
    }

    @Test
    void should_return_valid_bet_when_given_valid_inputs() {
        Bet result = betslipMapper.toBet(betRequestDTO, event, betslip);

        assertNotNull(result);
        assertEquals(betslip, result.getBetslip());
        assertEquals(event, result.getEvent());
        assertEquals(BetType.HOME_WIN, result.getSelectedBetType());
        assertEquals(2.10, result.getOdds());
    }

    @Test
    void should_set_correct_odds_when_different_bet_type_selected() {
        betRequestDTO.setSelectedBetType(BetType.DRAW);

        Bet result = betslipMapper.toBet(betRequestDTO, event, betslip);

        assertNotNull(result);
        assertEquals(BetType.DRAW, result.getSelectedBetType());
        assertEquals(3.40, result.getOdds());
    }

    @Test
    void should_throw_exception_when_betslip_request_is_null() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> betslipMapper.toBetslip(null, "customer123"));
        assertEquals("CreateBetslipRequest cannot be null", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_bet_request_is_null() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> betslipMapper.toBet(null, event, betslip));
        assertEquals("BetRequestDTO cannot be null", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_event_is_null() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> betslipMapper.toBet(betRequestDTO, null, betslip));
        assertEquals("Event cannot be null", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_betslip_is_null() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> betslipMapper.toBet(betRequestDTO, event, null));
        assertEquals("Betslip cannot be null", exception.getMessage());
    }
} 