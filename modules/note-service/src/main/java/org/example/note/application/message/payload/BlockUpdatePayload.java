package org.example.note.application.message.payload;

import java.util.Map;

import org.example.note.domain.enums.ObjectType;

public record BlockUpdatePayload(
        Long blockId,
        Map<String, Object> updateFields
        ) implements MessagePayload {

    @Override
    public ObjectType getObjectType() {
        return ObjectType.BLOCK;
    }
}
