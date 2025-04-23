package org.example.note.adapter.in.ws;

import java.util.UUID;

import org.example.note.application.message.WebSocketMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishToProject(UUID projectId, WebSocketMessage message) {
        messagingTemplate.convertAndSend("/topic/project/" + projectId, message);
    }

    public void publishToNote(UUID projectId, UUID noteId, WebSocketMessage message) {
        messagingTemplate.convertAndSend("/topic/project/" + projectId + "/note/" + noteId, message);
    }
}
