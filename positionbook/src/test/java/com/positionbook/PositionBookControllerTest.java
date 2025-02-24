package com.positionbook;

import com.positionbook.controller.PositionBookController;
import com.positionbook.model.Position;
import com.positionbook.model.TradeEvent;
import com.positionbook.service.PositionBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PositionBookController.class)
public class PositionBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PositionBookService positionBookService;

    private List<Position> positions;

    @BeforeEach
    void setUp() {
        List<TradeEvent> tradeEvents = List.of(
                new TradeEvent(1, "BUY", "ACC1", "SEC1", 10),
                new TradeEvent(2, "SELL", "ACC1", "SEC1", 5)
        );

        positions = List.of(
                new Position("ACC1", "SEC1", 100, tradeEvents)
        );
    }

    @Test
    void testGetAllPositions() throws Exception {

        Mockito.when(positionBookService.getAllPositions()).thenReturn(positions);

        mockMvc.perform(get("/api/position-book/positions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.positions[0].account").value("ACC1"))
                .andExpect(jsonPath("$.positions[0].security").value("SEC1"))
                .andExpect(jsonPath("$.positions[0].quantity").value(100))
                .andExpect(jsonPath("$.positions[0].events[0].id").value(1))
                .andExpect(jsonPath("$.positions[0].events[1].id").value(2));

        verify(positionBookService, times(1)).getAllPositions();
    }

    @Test
    void testClearAllPositions() throws Exception {

        Mockito.doNothing().when(positionBookService).clearAllPositions();

        mockMvc.perform(delete("/api/position-book/clear-positions"))
                .andExpect(status().isOk())
                .andExpect(content().string("All Trade Events are removed"));

        verify(positionBookService, times(1)).clearAllPositions();
    }

    @Test
    void testProcessTradeEvents() throws Exception {

        Mockito.doNothing().when(positionBookService).processTradeEvents(Mockito.anyList());

        mockMvc.perform(post("/api/position-book/trade-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"events\": [" +
                                "  {\"id\": 1, \"action\": \"BUY\", \"account\": \"ACC1\", \"security\": \"SEC1\", \"quantity\": 10}," +
                                "  {\"id\": 2, \"action\": \"SELL\", \"account\": \"ACC1\", \"security\": \"SEC1\", \"quantity\": 5}" +
                                "]}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Trade Events are processed successfully"));

        verify(positionBookService, times(1)).processTradeEvents(Mockito.anyList());
    }
}
