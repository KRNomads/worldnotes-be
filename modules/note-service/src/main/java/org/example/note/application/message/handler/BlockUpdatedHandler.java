package org.example.note.application.message.handler;

import org.example.note.adapter.out.ws.WebSocketPublisher;
import org.example.note.application.BlockService;
import org.example.note.application.dto.BlockDto;
import org.example.note.application.message.WebSocketMessage;
import org.example.note.application.message.payload.BlockPayload;
import org.example.note.application.message.payload.BlockUpdatePayload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class BlockUpdatedHandler implements WebSocketMessageHandler {

    private final WebSocketPublisher publisher;
    private final ObjectMapper objectMapper;
    private final BlockService blockService;

    @Override
    public WebSocketMessage.MessageType getType() {
        return WebSocketMessage.MessageType.BLOCK_UPDATED;
    }

    @Override
    public <T> void handle(WebSocketMessage<T> message) {
        BlockUpdatePayload payload = (BlockUpdatePayload) message.getPayload();

        // 블록 콘텐츠 업데이트
        BlockDto updatedBlock = blockService.update(
            payload.blockId(),
            payload.updateFields()
        );

        // 갱신된 블록 정보를 포함한 새 메시지 생성
        WebSocketMessage<BlockPayload> newMessage = WebSocketMessage.of(
            message.getType(),
            message.getUserId(),
            BlockPayload.fromDto(updatedBlock)
        );

        // // 노트 단위 브로드캐스트   수정 필요 !!!!!!!!!!!!
        // publisher.publishToNote(
        //     updatedBlock.projectId(),
        //     updatedBlock.noteId(),
        //     newMessage
        // );

        log.debug("🔄 블록이 수정되었습니다. blockId={}, userId={}", payload.blockId(), message.getUserId());
    }
}
