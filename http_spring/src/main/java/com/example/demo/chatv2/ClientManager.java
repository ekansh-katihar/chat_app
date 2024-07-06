package com.example.demo.chatv2;

import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ClientManager {
    private final ConcurrentMap<String, FluxSink<String>> clients = new ConcurrentHashMap<>();

    public void registerClient(String clientId, FluxSink<String> sink) {
        clients.put(clientId, sink);
    }

    public void unregisterClient(String clientId) {
        clients.remove(clientId);
    }

    public void sendMessageToClient(String clientId, String message) {
        FluxSink<String> sink = clients.get(clientId);
        if (sink != null) {
            sink.next(message);
        }
    }
}
