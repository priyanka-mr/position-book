package com.positionbook.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Position {
    private String account;
    private String security;
    private int quantity;
    private List<TradeEvent> events = new ArrayList<>();

    public Position() {};

    public Position(String account, String security) {
        this.account = account;
        this.security = security;
        this.quantity = 0;
    }

    public Position(String account, String security, int quantity, List<TradeEvent> events) {
        this.account = account;
        this.security = security;
        this.quantity = quantity;
        this.events = events;
    }

    public void updateQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void addEvent(TradeEvent event) {
        this.events.add(event);
    }

}
