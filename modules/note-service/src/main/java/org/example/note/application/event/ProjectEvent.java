package org.example.note.application.event;

import java.util.UUID;

import org.example.note.application.dto.ProjectDto;
import org.example.note.application.message.WebSocketMessage;
import org.example.note.application.message.payload.ProjectPayload;

import lombok.Getter;

@Getter
public class ProjectEvent {

    public enum EventType {
        CREATED, UPDATED, DELETED
    }

    private final EventType eventType;
    private final UUID userId;
    private final ProjectDto object;

    private ProjectEvent(EventType eventType, UUID userId, ProjectDto projectDto) {
        this.eventType = eventType;
        this.userId = userId;
        this.object = projectDto;
    }

    public static ProjectEvent created(UUID userId, ProjectDto projectDto) {
        return new ProjectEvent(EventType.CREATED, userId, projectDto);
    }

    public static ProjectEvent updated(UUID userId, ProjectDto projectDto) {
        return new ProjectEvent(EventType.UPDATED, userId, projectDto);
    }

    public static ProjectEvent deleted(UUID userId, ProjectDto projectDto) {
        return new ProjectEvent(EventType.DELETED, userId, projectDto);
    }

    public WebSocketMessage<ProjectPayload> toWebSocketMessage() {
        WebSocketMessage.MessageType messageType
                = switch (eventType) {
            case CREATED ->
                WebSocketMessage.MessageType.PROJECT_CREATED;
            case UPDATED ->
                WebSocketMessage.MessageType.PROJECT_UPDATED;
            case DELETED ->
                WebSocketMessage.MessageType.PROJECT_DELETED;
            default ->
                throw new IllegalArgumentException("Unknown event type: " + eventType);
        };

        return WebSocketMessage.of(messageType, userId, ProjectPayload.fromDto(object));
    }
}
