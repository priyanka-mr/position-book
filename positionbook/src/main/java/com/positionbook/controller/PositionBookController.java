package com.positionbook.controller;

import com.positionbook.model.Position;
import com.positionbook.model.TradeEvent;
import com.positionbook.service.PositionBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/position-book")
public class PositionBookController {

    private final PositionBookService positionBookService;
    private static final Logger logger = LoggerFactory.getLogger(PositionBookController.class);

    public PositionBookController(PositionBookService positionBookService) {
        this.positionBookService = positionBookService;
    }

    @PostMapping("/trade-events")
    public String processTradeEvents(@RequestBody Map<String, List<TradeEvent>> request) {
        logger.info("Request received for processing a trade event");
        List<TradeEvent> tradeEvents = request.get("events");
        positionBookService.processTradeEvents(tradeEvents);
        logger.info("Processed {} trade events", tradeEvents.size());
        return "Trade Events are processed successfully";
    }

    @GetMapping("/positions")
    public Map<String, List<Position>> getAllPositions() {
        logger.info("Fetching all positions");
        Map<String, List<Position>> positions = Map.of("positions", positionBookService.getAllPositions());
        logger.info("Returning {} positions", positions.get("positions").size());
        return positions;
    }

    @DeleteMapping("/clear-positions")
    public String clearAllPositions() {
        logger.info("Received request to clear all positions");
        positionBookService.clearAllPositions();
        logger.info("Cleared all positions successfully");
        return "All Trade Events are removed";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleCustomServiceException(Exception ex) {
        logger.error("Error in processing: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
