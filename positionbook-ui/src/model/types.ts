export interface TradeEvent {
  id: number;
  action: 'BUY' | 'SELL' | 'CANCEL'
  account: string
  security: string
  quantity: number
}

export interface TradeEventObject {
  events: TradeEvent[]
}

export interface Position {
  account: string
  security: string
  quantity: number
  events: TradeEvent[]
}

export interface WebSocketMessage {
  positions: Position[]
}
