export type TradeEvent = {
    id: number;
    action: "BUY" | "SELL" | "CANCEL";
    account: string;
    security: string;
    quantity: number;
}

export type TradeEventObject = {
    events: TradeEvent[];
}

export type Position = {
    account: string;
    security: string;
    quantity: number;
    events: TradeEvent[];
}

export type WebSocketMessage = {
    positions: Position[];
}