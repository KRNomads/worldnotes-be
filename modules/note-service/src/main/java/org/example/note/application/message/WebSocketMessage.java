package org.example.note.application.message;

import java.util.UUID;

import lombok.Data;

@Data
public class WebSocketMessage<T> {

    public enum MessageType {
        // 프로젝트 이벤트
        PROJECT_CREATED,
        PROJECT_UPDATED,
        PROJECT_DELETED,
        // 노트 이벤트
        NOTE_CREATED,
        NOTE_UPDATED,
        NOTE_DELETED,
        // 블록 이벤트
        BLOCK_CREATED,
        BLOCK_UPDATED,
        BLOCK_DELETED,
        // 협업 상태
        CURSOR_MOVED,
        USER_JOINED,
        USER_LEFT
    }

    private MessageType type;
    private UUID userId;
    private T payload;

    private WebSocketMessage() {
    }

    public static <T> WebSocketMessage<T> of(MessageType type, UUID userId, T payload) {
        WebSocketMessage<T> message = new WebSocketMessage<>();
        message.type = type;
        message.userId = userId;
        message.payload = payload;
        return message;
    }
}
