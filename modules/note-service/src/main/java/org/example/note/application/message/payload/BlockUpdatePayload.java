package org.example.note.application.message.payload;

import java.util.Map;

import org.example.note.application.dto.BlockUpdateParam;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.enums.ObjectType;

public record BlockUpdatePayload(
        Long blockId,
        String title,
        BlockType type,
        Map<String, Object> properties // e.g. { "TEXT": { "content": "..." } }
        ) implements MessagePayload {

    @Override
    public ObjectType getObjectType() {
        return ObjectType.BLOCK;
    }

    public BlockUpdateParam toParam() {
        return new BlockUpdateParam(title, type, properties);
    }
}
