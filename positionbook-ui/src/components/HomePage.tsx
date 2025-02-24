import { useState } from "react";
import PositionTable from "./PositionTable";
import TradeEventForm from "./TradeEventForm";
import { Tab, Tabs, TabsBody, TabsHeader } from "@material-tailwind/react";

const HomePage = () => {
    const [activeTab, setActiveTab] = useState("position-summary");

    const tabs = [
        { 
            label: "Position Summary",
            value: "position-summary",
        },
        {
            label: "Create Event",
            value: "tradeevent-form",
        }
    ]

    const handleTabClick = (tab: string) => {
        setActiveTab(tab);
    };

    return (
        <div className="p-4 flex justify-center">
            <Tabs value={activeTab}>
                <div className="flex justify-center">
                    <TabsHeader
                    className="bg-white rounded-lg p-2"
                    indicatorProps={{
                        className: "bg-transparent border-b-4 rounded",
                    }}
                    >
                    {tabs.map(({ label, value }) => (
                        <Tab
                            key={value}
                            value={value}
                            onClick={() => handleTabClick(value)}
                            className={`px-6 py-2 font-bold w-100
                            ${activeTab === value ? "text-blue-700" 
                            : "text-gray-700 hover:text-gray-500"}`}
                        >
                            {label}
                        </Tab>
                    ))}
                    </TabsHeader>
                        </div>
                        <div className="bg-white p-6 mt-4">
                        <TabsBody>
                        {activeTab === "position-summary" && <PositionTable />}
                        {activeTab === "tradeevent-form" && <TradeEventForm />}
                    </TabsBody>
                </div>
            </Tabs>
        </div>
    )
}

export default HomePage;

  