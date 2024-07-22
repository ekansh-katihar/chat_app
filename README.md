## Introduction
*chatv2:* The purpose of this POC to create a chat server using WebSockets and simulate a chat server using HTTP without the H2. The only requirement is if a user A sends the message to the user B, it should be received by user B And not by anyone else. Whether private key for https can be stolen, or user can impersonate other user is beyond the scope. 

*h2_stream:* Project to demo the REST streaming api response (partial success) simply run StockController.java. Access the https://localhost:8080/api/...

## HTTP2 and HTTP1.1
Checkout http_spring project which demonstrate chat server functionality using HTTP1.1 and HTTPS/2 built using Java and SpringWebFlux.

## Websocket
Checkout websocket_fast_api_py project which demonstrate chat server functionality using Websocket built using Python, FastAPI and Uvicorn serve.
