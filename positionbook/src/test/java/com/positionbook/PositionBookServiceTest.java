package com.positionbook;

import com.positionbook.model.Position;
import com.positionbook.model.TradeEvent;
import com.positionbook.repository.PositionBookRepository;
import com.positionbook.service.PositionBookService;
import com.positionbook.websocket.WebSocketHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PositionBookServiceTest {

    @Mock
    private WebSocketHandler webSocketHandler;

    @Autowired
    private PositionBookRepository positionBookRepository;

    @Autowired
    private PositionBookService positionBookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        positionBookService.clearAllPositions();
    }

    @Test
    void testProcessTradeEvents_BuyEvent() {
        TradeEvent tradeEvent = new TradeEvent(1,"BUY", "ACC1", "SEC1", 100);
        positionBookService.processTradeEvents(List.of(tradeEvent));
        List<Position> positionList = positionBookService.getAllPositions();
        assertEquals(1, positionList.size());
        assertEquals("ACC1", positionList.getFirst().getAccount());
        assertEquals("SEC1", positionList.getFirst().getSecurity());
    }

    @Test
    void testProcessTradeEvents_SellEvent() {
        TradeEvent tradeEvent1 = new TradeEvent(2,"BUY", "ACC1", "SEC1", 100);
        TradeEvent tradeEvent2 = new TradeEvent(3,"SELL", "ACC1", "SEC1", 100);
        positionBookService.processTradeEvents(List.of(tradeEvent1, tradeEvent2));
        List<Position> positionList = positionBookService.getAllPositions();
        assertEquals(1, positionList.size());
        assertEquals(2, positionList.getFirst().getEvents().size());
        assertEquals(0, positionList.getFirst().getQuantity());
    }

    @Test
    void testProcessTradeEvents_CancelBuyEvent() {
        TradeEvent tradeEvent1 = new TradeEvent(4,"BUY", "ACC2", "SEC2", 100);
        TradeEvent tradeEvent2 = new TradeEvent(4,"CANCEL", "ACC2", "SEC2", 0);
        positionBookService.processTradeEvents(List.of(tradeEvent1, tradeEvent2));
        List<Position> positionList = positionBookService.getAllPositions();
        assertEquals(1, positionList.size());
        assertEquals("ACC2", positionList.getFirst().getAccount());
        assertEquals("SEC2", positionList.getFirst().getSecurity());
        assertEquals(0, positionList.getFirst().getQuantity());
    }

    @Test
    void testProcessTradeEvents_CancelSellEvent() {
        TradeEvent tradeEvent0 = new TradeEvent(5,"BUY", "ACC3", "SEC3", 200);
        TradeEvent tradeEvent1 = new TradeEvent(6,"SELL", "ACC3", "SEC3", 100);
        TradeEvent tradeEvent2 = new TradeEvent(6,"CANCEL", "ACC3", "SEC3", 0);
        positionBookService.processTradeEvents(List.of(tradeEvent0, tradeEvent1, tradeEvent2));
        List<Position> positionList = positionBookService.getAllPositions();
        assertEquals(1, positionList.size());
        assertEquals("ACC3", positionList.getFirst().getAccount());
        assertEquals("SEC3", positionList.getFirst().getSecurity());
        assertEquals(200, positionList.getFirst().getQuantity());
    }

    @Test
    void testProcessTradeEvents_BuyDifferentSecuritiesEvent() {
        TradeEvent tradeEvent0 = new TradeEvent(7,"BUY", "ACC4", "SECXYZ", 200);
        TradeEvent tradeEvent1 = new TradeEvent(8,"BUY", "ACC4", "SEC4", 100);
        TradeEvent tradeEvent2 = new TradeEvent(9,"BUY", "ACC4", "SECXYZ", 50);
        positionBookService.processTradeEvents(List.of(tradeEvent0, tradeEvent1, tradeEvent2));
        List<Position> positionList = positionBookService.getAllPositions();
        assertEquals(2, positionList.size());
        Position secXYZPosition = positionList.stream()
                .filter(pos -> pos.getSecurity().equals("SECXYZ"))
                .findFirst()
                .orElseThrow();
        assertEquals(250, secXYZPosition.getQuantity());

        Position sec4Position = positionList.stream()
                .filter(pos -> pos.getSecurity().equals("SEC4"))
                .findFirst()
                .orElseThrow();
        assertEquals(100, sec4Position.getQuantity());
    }
}
