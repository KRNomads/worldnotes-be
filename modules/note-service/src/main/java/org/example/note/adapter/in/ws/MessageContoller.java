package org.example.note.adapter.in.ws;

import java.util.UUID;

import org.example.note.application.MessageHandleService;
import org.example.note.application.message.WebSocketMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MessageContoller {

    private final MessageHandleService messageHandleService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Project Message
     */
    @MessageMapping("/project/{projectId}")
    public void handleProjectSpace(@DestinationVariable UUID projectId, WebSocketMessage message) {

        log.info("Handle project message {}: {}", projectId, message);

        try {
            WebSocketMessage response = null;

            switch (message.getType()) {
                case "note_create":
                    response = messageHandleService.handleNoteCreate(message);
                    break;
                case "note_edit":
                    response = messageHandleService.handleNoteEdit(message);
                    break;
                case "note_delete":
                    response = messageHandleService.handleNoteDelete(message);
                    break;
                case "project_edit":
                    response = messageHandleService.handleProjectEdit(message);
                    break;
            }

            if (response != null) {
                messagingTemplate.convertAndSend("/topic/project/" + projectId, response);
            }
        } catch (Exception e) {
            log.error("Failed to handle message", e);
            throw e;
        }
    }

    /**
     * Note Message
     */
    @MessageMapping("/project/{projectId}/note/{noteId}")
    public void handleNoteSpace(
            @DestinationVariable UUID projectId,
            @DestinationVariable UUID noteId,
            WebSocketMessage message) {

        log.info("Handle note message {}: {}", noteId, message);

        try {
            WebSocketMessage response = null;

            switch (message.getType()) {
                case "block_create":
                    response = messageHandleService.handleBlockCreate(message);
                    break;
                case "block_edit":
                    response = messageHandleService.handleBlockEdit(message);
                    break;
                case "block_delete":
                    response = messageHandleService.handleBlockDelete(message);
                    break;
            }

            if (response != null) {
                messagingTemplate.convertAndSend("/topic/project/" + projectId + "/note/" + noteId, response);
            }
        } catch (Exception e) {
            log.error("Failed to update block", e);
            throw e;
        }
    }

}
