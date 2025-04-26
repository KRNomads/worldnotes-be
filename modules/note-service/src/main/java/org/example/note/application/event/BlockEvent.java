package org.example.note.application.event;

import java.util.UUID;

import org.example.note.application.dto.BlockDto;
import org.example.note.application.message.WebSocketMessage;
import org.example.note.application.message.payload.BlockPayload;

import lombok.Getter;

@Getter
public class BlockEvent implements NoteObjectEvent<BlockDto, BlockPayload> {

    private final EventType eventType;
    private final UUID userId;
    private final BlockDto object;

    private BlockEvent(EventType eventType, UUID userId, BlockDto blockDto) {
        this.eventType = eventType;
        this.userId = userId;
        this.object = blockDto;
    }

    public static BlockEvent created(UUID userId, BlockDto blockDto) {
        return new BlockEvent(EventType.CREATED, userId, blockDto);
    }

    public static BlockEvent updated(UUID userId, BlockDto blockDto) {
        return new BlockEvent(EventType.UPDATED, userId, blockDto);
    }

    public static BlockEvent deleted(UUID userId, BlockDto blockDto) {
        return new BlockEvent(EventType.DELETED, userId, blockDto);
    }

    @Override
    public WebSocketMessage<BlockPayload> toWebSocketMessage() {
        WebSocketMessage.MessageType messageType
                = switch (eventType) {
            case CREATED ->
                WebSocketMessage.MessageType.BLOCK_CREATED;
            case UPDATED ->
                WebSocketMessage.MessageType.BLOCK_UPDATED;
            case DELETED ->
                WebSocketMessage.MessageType.BLOCK_DELETED;
        };

        return WebSocketMessage.of(messageType, userId, BlockPayload.fromDto(object));
    }
}
