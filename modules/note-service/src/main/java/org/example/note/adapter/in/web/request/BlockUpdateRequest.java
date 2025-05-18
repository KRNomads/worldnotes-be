package org.example.note.adapter.in.web.request;

import java.util.Map;

import org.example.note.application.dto.BlockUpdateParam;
import org.example.note.domain.enums.BlockType;

public record BlockUpdateRequest(
        String title,
        BlockType type,
        Map<String, Object> properties
        ) {

    public BlockUpdateParam toParam() {
        return new BlockUpdateParam(title, type, properties);
    }
}
