import axios from "axios";
import { Position, TradeEventObject } from "../model/types";


const API_URL = "http://localhost:8080/api/position-book";

export const getPositions = async () : Promise<Position[]> => {
    const response = await axios.get(`${API_URL}/positions`);
    return response.data.positions;
}

export const sendTradeEvent = async (tradeEvent: TradeEventObject) : Promise<string> => {
    const response = await axios.post(`${API_URL}/trade-events`, tradeEvent);
    return response.data;
}
