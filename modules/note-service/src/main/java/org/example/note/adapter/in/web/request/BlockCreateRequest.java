package org.example.note.adapter.in.web.request;

import java.util.Map;
import java.util.UUID;

import org.example.note.application.dto.BlockCreateParam;
import org.example.note.domain.enums.BlockType;

public record BlockCreateRequest(
        UUID noteId,
        String title,
        String fieldKey,
        BlockType type,
        Map<String, Object> properties) {

    public BlockCreateParam toParam() {
        return new BlockCreateParam(
                title,
                fieldKey,
                type,
                properties
        );
    }
}
