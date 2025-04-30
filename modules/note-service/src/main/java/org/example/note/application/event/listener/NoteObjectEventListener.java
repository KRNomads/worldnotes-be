package org.example.note.application.event.listener;

import org.example.note.adapter.out.ws.WebSocketPublisher;
import org.example.note.application.event.BlockEvent;
import org.example.note.application.event.NoteEvent;
import org.example.note.application.event.ProjectEvent;
import org.example.note.application.message.WebSocketMessage;
import org.example.note.application.message.payload.BlockPayload;
import org.example.note.application.message.payload.NotePayload;
import org.example.note.application.message.payload.ProjectPayload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class NoteObjectEventListener {

    private final WebSocketPublisher webSocketPublisher;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProjectEvent(ProjectEvent event) {
        WebSocketMessage<ProjectPayload> message = event.toWebSocketMessage();
        webSocketPublisher.publishToProject(message.getPayload().projectId(), message);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNoteEvent(NoteEvent event) {
        WebSocketMessage<NotePayload> message = event.toWebSocketMessage();
        webSocketPublisher.publishToProject(message.getPayload().projectId(), message);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBlockEvent(BlockEvent event) {
        WebSocketMessage<BlockPayload> message = event.toWebSocketMessage();
        webSocketPublisher.publishToNote(message.getPayload().noteId(), message);
    }

}
