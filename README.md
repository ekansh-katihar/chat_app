## Introduction
The purpose of this POC to create a chat server using WebSockets,HTTP 1.1 and HTTP2. The only requirement is if a user A sends the message to the user B, it should be received by user B And not by anyone else. Whether private key for https can be stolen, or user can impersonate other user is beyond the scope. 

## HTTP2 and HTTP1.1
Checkout http_spring project which demonstrate chat server functionality using HTTP1.1 and HTTPS/2 built using Java and SpringWebFlux.

## Websocket
Checkout websocket_fast_api_py project which demonstrate chat server functionality using Websocket built using Python, FastAPI and Uvicorn serve.
