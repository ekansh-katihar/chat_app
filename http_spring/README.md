## Introduction
The purpose of this POC is to simulate the the chat server using http ( without multiplexing). The only requirement is if a user A sends the message to the user B, it should be received by user B And not by anyone else. Whether private key for https can be stolen, or user can impersonate other user is beyond the scope. 

## Run the app with HTTPS and HTTP2 mode
 - Run ChatServerApplication to start the server.
 - Go to https://localhost:8080/index.html in your browser. Tell the browser to accept the certificate.

## Run the app with HTTPS and HTTP1.1 mode
First go to application.properties and do what's mentioned in "## To run this server in HTTP1.1 mode comment everything below"
Run ChatServerApplication to start the server.
Go to https://localhost:8080/index.html in your browser. Tell the browser to accept the certificate.

## Testing the application
 - Enter the client id : A
 - Open other window and enter the client id : B
 - Enter the message  in this format {From Client Id} {To client id } {Message}
 - Example : A B Hello