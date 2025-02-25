package com.positionbook.service;

import com.positionbook.controller.PositionBookController;
import com.positionbook.model.Position;
import com.positionbook.model.TradeEvent;
import com.positionbook.repository.PositionBookRepository;
import com.positionbook.websocket.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class PositionBookService {
    private final PositionBookRepository positionBookRepository;
    private final WebSocketHandler webSocketHandler;
    private static final Logger logger = LoggerFactory.getLogger(PositionBookService.class);

    public PositionBookService(PositionBookRepository positionBookRepository, WebSocketHandler webSocketHandler) {
        this.positionBookRepository = positionBookRepository;
        this.webSocketHandler = webSocketHandler;
    }

    public void processTradeEvents(List<TradeEvent> tradeEvents) {
        try {
            for (TradeEvent tradeEvent : tradeEvents) {
                Position position = positionBookRepository.getOrCreatePosition(tradeEvent.getAccount(),
                        tradeEvent.getSecurity());

                if ("CANCEL".equalsIgnoreCase(tradeEvent.getAction())) {
                    logger.info("processing CANCEL event");
                    cancelEvent(position, tradeEvent);
                    position.addEvent(tradeEvent);
                    continue;
                }

                int quantityChange = "BUY".equalsIgnoreCase(tradeEvent.getAction()) ?
                        tradeEvent.getQuantity() : -tradeEvent.getQuantity();
                position.updateQuantity(quantityChange);
                position.addEvent(tradeEvent);
                logger.info("{} {} Position updated with a {} event "
                        ,position.getAccount(), position.getSecurity(), tradeEvent.getAction());
            }

            webSocketHandler.broadcastPositions(getAllPositions());
        } catch (IOException e) {
            logger.error("Error processing trade events" ,e);
        }
    }

    private void cancelEvent(Position position, TradeEvent cancelEvent) {
        for (TradeEvent event : position.getEvents()) {
            if (event.getId() == cancelEvent.getId() && "BUY".equalsIgnoreCase(event.getAction())
                    || "SELL".equalsIgnoreCase(event.getAction())) {
                int reverseQuantity = "BUY".equalsIgnoreCase(event.getAction()) ?
                        -event.getQuantity() : event.getQuantity();
                position.updateQuantity(reverseQuantity);
                break;
            }
        }
    }

    public List<Position> getAllPositions() {
        return List.copyOf(positionBookRepository.getAllPositions().values());
    }

    public void clearAllPositions() {
        positionBookRepository.clearAllPositions();
    }
}
