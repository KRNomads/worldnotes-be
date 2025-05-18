package org.example.note.domain.template;

import org.example.note.domain.enums.BlockType;
import org.example.note.domain.property.BlockProperties;

public record BlockTemplate(
        String title,
        String fieldKey,
        BlockType type,
        BlockProperties properties,
        Integer position
        ) {

}
