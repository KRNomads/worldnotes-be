package org.example.note.application.event;

import java.util.UUID;

import org.example.note.application.dto.NoteObjectDto;
import org.example.note.application.message.WebSocketMessage;
import org.example.note.application.message.payload.MessagePayload;

public interface NoteObjectEvent<T extends NoteObjectDto, K extends MessagePayload> {

    public enum EventType {
        CREATED, UPDATED, DELETED
    }

    EventType getEventType();

    UUID getUserId();

    T getObject();

    // 웹소켓 메시지로 변환
    public WebSocketMessage<K> toWebSocketMessage();
}
