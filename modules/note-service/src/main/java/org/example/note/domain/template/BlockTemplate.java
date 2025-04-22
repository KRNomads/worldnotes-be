package org.example.note.domain.template;

import java.util.Map;

import org.example.note.domain.enums.BlockType;

public record BlockTemplate(
        String title,
        boolean isDefault,
        BlockType type,
        Map<String, Object> content,
        Integer position
        ) {

}
