from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from fastapi.staticfiles import StaticFiles
from typing import Optional
from starlette.responses import FileResponse

import pdb
import json

app = FastAPI()
connected_clients = {}  # Dictionary to store connected clients (client_id: websocket)

# Mount the static files directory
app.mount("/static", StaticFiles(directory="static"), name="static")

@app.get("/")
async def read_index():
    return FileResponse("static/index.html")


async def broadcast_message(message: str, sender: Optional[str] = None):
    # Broadcast message to all connected clients except the sender (if provided)
    for client_id, websocket in connected_clients.items():
        if websocket.closed:  # Check if connection is closed
            continue
        if client_id != sender:
            await websocket.send_text(message)

@app.websocket("/ws/{client_id}")
async def websocket_endpoint(websocket: WebSocket, client_id: str):
    connected_clients[f"@{client_id}"] = websocket
    try:
        await websocket.accept()  # Accept the WebSocket connection
        while True:
            data_str = await websocket.receive_text()
            # pdb.set_trace()
            data_dict=json.loads(data_str)
            data=data_dict['content']
            # Check for message starting with "@" indicating a specific recipient
            if data.startswith("@"):
                recipient, message = data.split(" ", 1)
                # Check if recipient is connected
                if recipient in connected_clients:
                    
                    await connected_clients[recipient].send_text(json.dumps({"content": message}))
                else:
                    await websocket.send_text(f"Error: Recipient {recipient} not found")
            else:
                # Broadcast message to all clients
                await broadcast_message(f"{client_id}: {data}", client_id)
    except WebSocketDisconnect:
        await connected_clients.pop(client_id)
        await broadcast_message(f"{client_id} disconnected")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("server:app", host="0.0.0.0", port=9090)

