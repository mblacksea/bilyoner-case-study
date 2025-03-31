package com.bilyoner.service;

import com.bilyoner.dto.BetRequestDTO;
import com.bilyoner.dto.CreateBetslipRequest;
import com.bilyoner.mapper.BetslipMapper;
import com.bilyoner.model.Bet;
import com.bilyoner.model.BetType;
import com.bilyoner.model.Betslip;
import com.bilyoner.model.Event;
import com.bilyoner.repository.BetslipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BetslipServiceTest {

    @Mock
    private BetslipRepository betslipRepository;

    @Mock
    private EventService eventService;

    @Mock
    private BetslipMapper betslipMapper;

    @InjectMocks
    private BetslipService betslipService;

    private CreateBetslipRequest createBetslipRequest;
    private BetRequestDTO betRequestDTO;
    private Event event;
    private Betslip betslip;
    private Bet bet;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(betslipService, "maxBetsPerEvent", 500);

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

        bet = new Bet();
        bet.setId(1L);
        bet.setBetslip(betslip);
        bet.setEvent(event);
        bet.setSelectedBetType(BetType.HOME_WIN);
        bet.setOdds(2.10);
    }

    @Test
    void should_create_betslip_successfully_when_all_conditions_are_met() {
        String customerId = "customer123";
        when(betslipRepository.countBetslipsByEventIdAndCustomerId(anyLong(), anyString())).thenReturn(0L);
        when(eventService.getEventById(anyLong())).thenReturn(event);
        when(betslipMapper.toBetslip(any(), anyString())).thenReturn(betslip);
        when(betslipMapper.toBet(any(), any(), any())).thenReturn(bet);
        when(betslipRepository.save(any(Betslip.class))).thenReturn(betslip);

        Betslip result = betslipService.createBetslip(createBetslipRequest, customerId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(customerId, result.getCustomerId());
        assertEquals(new BigDecimal("100"), result.getAmount());
        assertEquals(2, result.getMultipleCount());

        verify(betslipRepository, times(1)).countBetslipsByEventIdAndCustomerId(eq(1L), eq(customerId));
        verify(eventService, times(1)).getEventById(eq(1L));
        verify(betslipMapper, times(1)).toBetslip(eq(createBetslipRequest), eq(customerId));
        verify(betslipMapper, times(1)).toBet(eq(betRequestDTO), eq(event), eq(betslip));
        verify(betslipRepository, times(1)).save(eq(betslip));
    }

    @Test
    void should_throw_exception_when_maximum_bet_limit_exceeded() {
        String customerId = "customer123";
        when(betslipRepository.countBetslipsByEventIdAndCustomerId(anyLong(), anyString())).thenReturn(499L);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            betslipService.createBetslip(createBetslipRequest, customerId);
        });

        assertTrue(exception.getMessage().contains("Maximum bet limit"));
        
        verify(betslipRepository, times(1)).countBetslipsByEventIdAndCustomerId(eq(1L), eq(customerId));
        verify(eventService, never()).getEventById(anyLong());
        verify(betslipMapper, times(1)).toBetslip(eq(createBetslipRequest), eq(customerId));
        verify(betslipMapper, never()).toBet(any(), any(), any());
        verify(betslipRepository, never()).save(any(Betslip.class));
    }

    @Test
    void should_handle_odds_changes_when_creating_betslip() {
        String customerId = "customer123";
        betRequestDTO.setExpectedOdds(2.50);
        
        when(betslipRepository.countBetslipsByEventIdAndCustomerId(anyLong(), anyString())).thenReturn(0L);
        when(eventService.getEventById(anyLong())).thenReturn(event);
        when(betslipMapper.toBetslip(any(), anyString())).thenReturn(betslip);
        when(betslipMapper.toBet(any(), any(), any())).thenReturn(bet);
        when(betslipRepository.save(any(Betslip.class))).thenReturn(betslip);

        Betslip result = betslipService.createBetslip(createBetslipRequest, customerId);

        assertNotNull(result);
        
        verify(betslipRepository, times(1)).countBetslipsByEventIdAndCustomerId(eq(1L), eq(customerId));
        verify(eventService, times(1)).getEventById(eq(1L));
        verify(betslipMapper, times(1)).toBetslip(eq(createBetslipRequest), eq(customerId));
        verify(betslipMapper, times(1)).toBet(eq(betRequestDTO), eq(event), eq(betslip));
        verify(betslipRepository, times(1)).save(eq(betslip));
    }

    @Test
    void should_process_multiple_bets_when_creating_betslip() {
        String customerId = "customer123";
        
        BetRequestDTO secondBetRequest = new BetRequestDTO();
        secondBetRequest.setEventId(2L);
        secondBetRequest.setSelectedBetType(BetType.DRAW);
        secondBetRequest.setExpectedOdds(3.40);
        createBetslipRequest.getBets().add(secondBetRequest);
        
        Event secondEvent = new Event();
        secondEvent.setId(2L);
        secondEvent.setLeagueName("La Liga");
        secondEvent.setHomeTeam("Barcelona");
        secondEvent.setAwayTeam("Real Madrid");
        secondEvent.setHomeWinOdds(1.90);
        secondEvent.setDrawOdds(3.40);
        secondEvent.setAwayWinOdds(3.80);
        
        Bet secondBet = new Bet();
        secondBet.setId(2L);
        secondBet.setBetslip(betslip);
        secondBet.setEvent(secondEvent);
        secondBet.setSelectedBetType(BetType.DRAW);
        secondBet.setOdds(3.40);
        
        when(betslipRepository.countBetslipsByEventIdAndCustomerId(eq(1L), anyString())).thenReturn(0L);
        when(betslipRepository.countBetslipsByEventIdAndCustomerId(eq(2L), anyString())).thenReturn(0L);
        when(eventService.getEventById(eq(1L))).thenReturn(event);
        when(eventService.getEventById(eq(2L))).thenReturn(secondEvent);
        when(betslipMapper.toBetslip(any(), anyString())).thenReturn(betslip);
        when(betslipMapper.toBet(eq(betRequestDTO), eq(event), eq(betslip))).thenReturn(bet);
        when(betslipMapper.toBet(eq(secondBetRequest), eq(secondEvent), eq(betslip))).thenReturn(secondBet);
        when(betslipRepository.save(any(Betslip.class))).thenReturn(betslip);

        Betslip result = betslipService.createBetslip(createBetslipRequest, customerId);

        assertNotNull(result);
        
        verify(betslipRepository, times(1)).countBetslipsByEventIdAndCustomerId(eq(1L), eq(customerId));
        verify(betslipRepository, times(1)).countBetslipsByEventIdAndCustomerId(eq(2L), eq(customerId));
        verify(eventService, times(1)).getEventById(eq(1L));
        verify(eventService, times(1)).getEventById(eq(2L));
        verify(betslipMapper, times(1)).toBetslip(eq(createBetslipRequest), eq(customerId));
        verify(betslipMapper, times(1)).toBet(eq(betRequestDTO), eq(event), eq(betslip));
        verify(betslipMapper, times(1)).toBet(eq(secondBetRequest), eq(secondEvent), eq(betslip));
        verify(betslipRepository, times(1)).save(eq(betslip));
    }
} 