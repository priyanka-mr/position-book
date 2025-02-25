import { useEffect, useState } from 'react'
import { Position, WebSocketMessage } from '../model/types'
import { getPositions } from '../service/apiService'
import useWebSocket from 'react-use-websocket'
import * as React from 'react'

const WS_URL = 'ws://localhost:8080/ws/positions'

const PositionTable = () => {
  const [positions, setPositions] = useState<Position[]>([])
  const [expandedRows, setExpandedRows] = useState<number[]>([])

  const toggleRow = (index: number) => {
    if (expandedRows.includes(index)) {
      setExpandedRows(expandedRows.filter((rowIndex) => rowIndex !== index))
    } else {
      setExpandedRows([...expandedRows, index])
    }
  }

  useEffect(() => {
    getPositions().then(setPositions)
  }, [])

  const { lastJsonMessage } = useWebSocket(WS_URL, {
    shouldReconnect: () => true,
  })

  useEffect(() => {
    if (lastJsonMessage) {
      const message = lastJsonMessage as WebSocketMessage
      setPositions(message.positions)
    }
  }, [lastJsonMessage])

  return (
    <div className="p-4">
      <table className="w-full border-collapse border border-gray-300">
        <thead>
          <tr className="bg-gray-200">
            <th className="border p-2">Account</th>
            <th className="border p-2">Security</th>
            <th className="border p-2">Quantity</th>
          </tr>
        </thead>
        <tbody>
          {positions.length > 0 ? (
            positions.map((pos, index) => (
              <React.Fragment key={index}>
                <tr
                  onClick={() => toggleRow(index)}
                  className="cursor-pointer hover:text-blue-500"
                >
                  <td className="border p-2">{pos.account}</td>
                  <td className="border p-2">{pos.security}</td>
                  <td className="border p-2">{pos.quantity}</td>
                </tr>
                {expandedRows.includes(index) && (
                  <tr>
                    <td colSpan={3}>
                      <table className="mt-4 mb-4 border-collapse w-full">
                        <thead>
                          <tr className="bg-gray-100">
                            <th className="border p-2">ID</th>
                            <th className="border p-2">Action</th>
                            <th className="border p-2">Account</th>
                            <th className="border p-2">Security</th>
                            <th className="border p-2">Quantity</th>
                          </tr>
                        </thead>
                        <tbody>
                          {pos.events.map((event, eventIndex) => (
                            <tr key={eventIndex}>
                              <td className="border p-2">{event.id}</td>
                              <td className="border p-2">{event.action}</td>
                              <td className="border p-2">{event.account}</td>
                              <td className="border p-2">{event.security}</td>
                              <td className="border p-2">{event.quantity}</td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </td>
                  </tr>
                )}
              </React.Fragment>
            ))
          ) : (
            <tr>
              <td colSpan={3} className="text-center p-4">
                No Positions available
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}

export default PositionTable
