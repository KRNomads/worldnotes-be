package org.example.note.application.event;

import java.util.UUID;

import org.example.note.application.dto.NoteDto;
import org.example.note.application.message.WebSocketMessage;
import org.example.note.application.message.payload.NotePayload;

import lombok.Getter;

@Getter
public class NoteEvent {

    public enum EventType {
        CREATED, UPDATED, DELETED
    }

    private final EventType eventType;
    private final UUID userId;
    private final NoteDto object;

    private NoteEvent(EventType eventType, UUID userId, NoteDto noteDto) {
        this.eventType = eventType;
        this.userId = userId;
        this.object = noteDto;
    }

    public static NoteEvent created(UUID userId, NoteDto noteDto) {
        return new NoteEvent(EventType.CREATED, userId, noteDto);
    }

    public static NoteEvent updated(UUID userId, NoteDto noteDto) {
        return new NoteEvent(EventType.UPDATED, userId, noteDto);
    }

    public static NoteEvent deleted(UUID userId, NoteDto noteDto) {
        return new NoteEvent(EventType.DELETED, userId, noteDto);
    }

    public WebSocketMessage<NotePayload> toWebSocketMessage() {
        WebSocketMessage.MessageType messageType
                = switch (eventType) {
            case CREATED ->
                WebSocketMessage.MessageType.NOTE_CREATED;
            case UPDATED ->
                WebSocketMessage.MessageType.NOTE_UPDATED;
            case DELETED ->
                WebSocketMessage.MessageType.NOTE_DELETED;
        };

        return WebSocketMessage.of(messageType, userId, NotePayload.fromDto(object));
    }
}
