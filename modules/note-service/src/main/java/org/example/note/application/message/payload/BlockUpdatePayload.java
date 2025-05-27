package org.example.note.application.message.payload;

import java.util.Map;

import org.example.note.application.dto.BlockUpdateParam;
import org.example.note.domain.enums.BlockType;

public record BlockUpdatePayload(
        Long blockId,
        String title,
        BlockType type,
        Map<String, Object> properties // e.g. { "TEXT": { "content": "..." } }
        ) implements MessagePayload {

    public BlockUpdateParam toParam() {
        return new BlockUpdateParam(title, type, properties);
    }
}
