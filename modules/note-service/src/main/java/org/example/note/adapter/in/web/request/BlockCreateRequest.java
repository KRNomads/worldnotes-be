package org.example.note.adapter.in.web.request;

import java.util.UUID;

import org.example.note.application.dto.BlockCreateParam;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.property.BlockProperties;

public record BlockCreateRequest(
        UUID noteId,
        String title,
        Boolean isDefault,
        String fieldKey,
        BlockType type,
        BlockProperties properties) {

    public BlockCreateParam toParam() {
        return new BlockCreateParam(
                title,
                isDefault,
                fieldKey,
                type,
                properties
        );
    }
}
