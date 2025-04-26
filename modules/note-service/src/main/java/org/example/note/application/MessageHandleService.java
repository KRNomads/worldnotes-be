package org.example.note.application;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.example.note.application.message.WebSocketMessage;
import org.example.note.application.message.handler.WebSocketMessageHandler;
import org.example.note.application.message.payload.MessagePayload;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MessageHandleService {

    private final List<WebSocketMessageHandler> handlers;
    private final Map<WebSocketMessage.MessageType, WebSocketMessageHandler> handlerMap = new EnumMap<>(WebSocketMessage.MessageType.class);

    @PostConstruct
    public void init() {
        for (WebSocketMessageHandler handler : handlers) {
            handlerMap.put(handler.getType(), handler);
        }
    }

    public <T extends MessagePayload> void handle(WebSocketMessage<T> message) {
        WebSocketMessageHandler handler = handlerMap.get(message.getType());
        if (handler != null) {
            handler.handle(message);
        } else {
            throw new IllegalArgumentException("처리할 수 없는 메시지 타입: " + message.getType());
        }
    }

}
