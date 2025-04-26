package org.example.note.application.message.handler;

import org.example.note.application.message.WebSocketMessage;

public interface WebSocketMessageHandler {

    WebSocketMessage.MessageType getType(); // 어떤 메시지 타입을 처리하는지

    <T> void handle(WebSocketMessage<T> message);
}
