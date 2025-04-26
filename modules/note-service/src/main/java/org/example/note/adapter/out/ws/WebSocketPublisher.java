package org.example.note.adapter.out.ws;

import java.util.UUID;

import org.example.note.application.message.WebSocketMessage;
import org.example.note.application.message.payload.MessagePayload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Project Message
     *
     * @param <T>
     */
    public <T extends MessagePayload> void publishToProject(UUID projectId, WebSocketMessage<T> message) {
        String destination = "/topic/project/" + projectId;

        messagingTemplate.convertAndSend(destination, message);
    }

    /**
     * Note Message
     *
     * @param <T>
     */
    public <T extends MessagePayload> void publishToNote(UUID noteId, WebSocketMessage<T> message) {
        String destination = "/topic/note/" + noteId;

        messagingTemplate.convertAndSend(destination, message);
    }
}
