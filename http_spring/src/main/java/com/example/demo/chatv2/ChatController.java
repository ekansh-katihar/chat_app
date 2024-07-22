package com.example.demo.chatv2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.demo.chatv2.ClientManager;

import reactor.core.publisher.Flux;
/**
 * Using Spring WebFlux
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ClientManager clientManager;

    @GetMapping(value = "/{clientId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@PathVariable String clientId) {
        return Flux.create(sink -> {
            clientManager.registerClient(clientId, sink);
            sink.onDispose(() -> clientManager.unregisterClient(clientId));
        });
    }

    @PostMapping("/send")
    public void sendMessage(@RequestParam String fromClientId, @RequestParam String toClientId, @RequestParam String message) {
        String formattedMessage = fromClientId + ": " + message;
        clientManager.sendMessageToClient(toClientId, formattedMessage);
    }
}