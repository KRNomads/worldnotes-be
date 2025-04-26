package org.example.note.application.message.payload;

import java.util.Map;
import java.util.UUID;

import org.example.note.application.dto.BlockDto;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.enums.ObjectType;

// 블록 관련 페이로드
public record BlockPayload(
        Long blockId,
        UUID noteId,
        String title,
        boolean isDefault,
        BlockType type,
        Map<String, Object> content,
        Integer position
        ) implements MessagePayload {

    @Override
    public ObjectType getObjectType() {
        return ObjectType.BLOCK;
    }

    // 필요한 팩토리 메서드
    public static BlockPayload fromDto(BlockDto dto) {
        return new BlockPayload(dto.blockId(), dto.noteId(), dto.title(), dto.isDefault(),
                dto.type(), dto.content(), dto.position());
    }
}
