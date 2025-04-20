package org.example.note.adapter.in.web.request;

import java.util.Map;
import java.util.UUID;

import org.example.note.domain.enums.BlockType;

public record BlockCreateRequest(
        UUID noteId,
        String title,
        Boolean isDefault,
        BlockType type,
        Map<String, Object> content,
        Integer position) {

}
