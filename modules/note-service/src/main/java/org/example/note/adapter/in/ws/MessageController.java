package org.example.note.adapter.in.ws;

import java.util.UUID;

import org.example.note.application.MessageHandleService;
import org.example.note.application.message.WebSocketMessage;
import org.example.note.application.message.payload.BlockPayload;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MessageController {

    private final MessageHandleService messageHandleService;

    // 블록 콘텐츠 실시간 업데이트
    @MessageMapping("/project/{projectId}/note/{noteId}/block")
    public void handleBlockUpdate(
            @DestinationVariable UUID projectId,
            @DestinationVariable UUID noteId,
            WebSocketMessage<BlockPayload> message) {

        log.info("Handle block content update: {}", message);

        messageHandleService.handle(message);
    }

    // // 커서 위치 등 협업 상태 업데이트
    // @MessageMapping("/project/{projectId}/note/{noteId}/cursor")
    // public void handleCursorMove(
    //         @DestinationVariable UUID projectId,
    //         @DestinationVariable UUID noteId,
    //         WebSocketMessage<CursorPayload> message) {
    //     // 커서 위치는 DB에 저장하지 않고 다른 클라이언트에게만 전달
    //     publisher.publishToNote(projectId, noteId, message);
    // }
}
