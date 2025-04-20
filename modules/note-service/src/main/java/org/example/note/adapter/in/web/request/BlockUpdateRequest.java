package org.example.note.adapter.in.web.request;

import java.util.Map;

import org.example.note.domain.enums.BlockType;

public record BlockUpdateRequest(
        String title,
        BlockType type,
        Map<String, Object> content,
        Integer position) {

}
