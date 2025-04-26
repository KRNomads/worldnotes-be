package org.example.note.application;

import org.example.note.adapter.out.ws.WebSocketPublisher;
import org.example.note.application.event.BlockEvent;
import org.example.note.application.event.NoteEvent;
import org.example.note.application.event.ProjectEvent;
import org.example.note.application.message.WebSocketMessage;
import org.example.note.application.message.payload.BlockPayload;
import org.example.note.application.message.payload.NotePayload;
import org.example.note.application.message.payload.ProjectPayload;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final WebSocketPublisher webSocketPublisher;

    public void notifyProjectChange(ProjectEvent event) {
        WebSocketMessage<ProjectPayload> message = event.toWebSocketMessage();
        webSocketPublisher.publishToProject(message.getPayload().projectId(), message);
    }

    public void notifyNoteChange(NoteEvent event) {
        WebSocketMessage<NotePayload> message = event.toWebSocketMessage();
        webSocketPublisher.publishToProject(message.getPayload().projectId(), message);
    }

    public void notifyBlockChange(BlockEvent event) {
        WebSocketMessage<BlockPayload> message = event.toWebSocketMessage();
        webSocketPublisher.publishToNote(message.getPayload().noteId(), message);
    }

}
