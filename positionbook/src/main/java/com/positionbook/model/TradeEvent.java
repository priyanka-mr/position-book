package com.positionbook.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TradeEvent {

    private int id;
    private String action;
    private String  account;
    private String security;
    private int quantity;

}
