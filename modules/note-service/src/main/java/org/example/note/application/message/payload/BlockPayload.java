package org.example.note.application.message.payload;

import java.util.UUID;

import org.example.note.application.dto.BlockDto;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.property.BlockProperties;

// 블록 관련 페이로드
public record BlockPayload(
        Long blockId,
        UUID noteId,
        String title,
        BlockType type,
        BlockProperties properties,
        Integer position
        ) implements MessagePayload {

    // 필요한 팩토리 메서드
    public static BlockPayload fromDto(BlockDto dto) {
        return new BlockPayload(dto.id(), dto.noteId(), dto.title(), dto.type(), dto.properties(), dto.position());
    }
}
