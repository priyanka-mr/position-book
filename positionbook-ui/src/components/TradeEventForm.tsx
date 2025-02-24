import * as React from "react";
import { useState } from "react";
import logger from "../logger";
import { TradeEvent } from "../model/types";
import { sendTradeEvent } from "../service/apiService";


const TradeEventForm = () => {
    const [tradeEvents, setTradeEvents] = useState<TradeEvent[]>([
        { id: 1, action: "BUY", account: "", security: "", quantity: 0 },
    ]);

    const handleinputChange = (index: number, field: string, value: any) => {
        const updatedTrades = [...tradeEvents];
        updatedTrades[index] = { ...updatedTrades[index], [field]: value };
        setTradeEvents(updatedTrades);
    };

    const addTradeEvent = () => {
        setTradeEvents([
            ...tradeEvents, 
            { id: tradeEvents.length + 1, 
            action: "BUY", account: "", security: "", quantity: 0 }])
    };

    const removeTradeEvent = (index: number) => {
        if (tradeEvents.length === 1) return;
        const updatedTrades = tradeEvents.filter((_, i) => i !== index);
        setTradeEvents(updatedTrades);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await sendTradeEvent({ events: tradeEvents }).then((response) => {
                alert(response);
            });
            logger.log("Trade Event processed successfully");
        } catch (error) {
            logger.error("Error processing trade events");
            alert("There was an error submitting the trade events");
        }
        setTradeEvents([{ id: 1, action: "BUY", account: "", security: "", quantity: 0 }]);
    };

    return (
        <div className="p-6">
            <form onSubmit={handleSubmit} className="space-y-4">
                {tradeEvents.map((trade, index) => (
                    <div key={index} className="flex flex-row items-center justify-center gap-4">
                         <div className="flex flex-col items-start">
                            <p className="mb-1 text-xs">
                                ID
                            </p>
                            <input 
                                type="number"
                                value={trade.id}
                                onChange={(e: any) => handleinputChange(index, "id", e.target.value)}
                                required
                                className="p-2 border border-gray-400 rounded-sm h-10 w-20"
                                />
                        </div>
                        <div className="flex flex-col items-start">
                            <p className="mb-1 text-xs">
                                TradeEvent
                            </p>
                            <select 
                                value={trade.action}
                                onChange={(e: any) => handleinputChange(index, "action", e.target.value)}
                                className="p-2 border border-gray-400 rounded-sm h-10 w-40"
                            >
                                <option value="BUY">BUY</option>
                                <option value="SELL">SELL</option>
                                <option value="CANCEL">CANCEL</option>
                            </select>
                        </div>
                        <div className="flex flex-col items-start">
                            <p className="mb-1 text-xs">
                                Account
                            </p>
                            <input 
                                type="text"
                                placeholder="Enter the account"
                                value={trade.account}
                                onChange={(e: any) => handleinputChange(index, "account", e.target.value)}
                                required
                                className="p-2 border border-gray-400 rounded-sm h-10 w-40"
                                />
                        </div>
                        <div className="flex flex-col items-start">
                            <p className="mb-1 text-xs">
                                Security
                            </p>
                            <input 
                                type="text"
                                placeholder="Enter the Security"
                                value={trade.security}
                                onChange={(e: any) => handleinputChange(index, "security", e.target.value)}
                                required
                                className="p-2 border border-gray-400 rounded-sm h-10 w-40"
                                />
                        </div>
                        <div className="flex flex-col items-start">
                            <p className="mb-1 text-xs">
                                Quantity
                            </p>
                            <input 
                                type="number"
                                value={trade.quantity}
                                onChange={(e: any) => handleinputChange(index, "quantity", e.target.value)}
                                required
                                disabled={trade.action === "CANCEL"}
                                className="p-2 border border-gray-400 rounded-sm h-10 w-40"
                                />
                        </div>
                        <div className="flex flex-col items-start">
                            <button 
                                type="button"
                                onClick={addTradeEvent}
                                className="mt-5 p-2 border border-blue-500 rounded-lg 
                                h-10 w-10 text-blue-500 hover:text-blue-700 cursor-pointer"
                                > + </button>
                        </div>
                        <div className="flex flex-col items-start">
                            <button 
                                type="button"
                                onClick={() => removeTradeEvent(index)}
                                className="mt-5 p-2 border border-red-500 rounded-lg 
                                h-10 w-10 text-red-500 hover:text-red-700 cursor-pointer"
                                > - </button>
                        </div>
                    </div>
                ))}
                <div className="space-x-4">
                <button type="submit"
                    className="bg-green-500 text-white px-6 py-2 rounded hover:bg-green-600"
                    >Submit Trade Events</button>
                </div>
            </form>
        </div>
    )
}

export default TradeEventForm;
