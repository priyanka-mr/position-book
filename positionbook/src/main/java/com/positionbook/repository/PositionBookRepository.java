package com.positionbook.repository;

import com.positionbook.model.Position;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PositionBookRepository {
    private final Map<String, Position> positionMap = new ConcurrentHashMap<>();

    public Position getOrCreatePosition(String account, String security) {
        return positionMap.computeIfAbsent(account + "-" + security,
                k -> new Position(account, security));
    }

    public Map<String, Position> getAllPositions() {
        return positionMap;
    }

    public void clearAllPositions() {
        positionMap.clear();
    }
}
