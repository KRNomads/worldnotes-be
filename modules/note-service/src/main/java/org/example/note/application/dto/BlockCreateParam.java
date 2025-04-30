package org.example.note.application.dto;

import org.example.note.domain.enums.BlockType;
import org.example.note.domain.property.BlockProperties;

public record BlockCreateParam(
        String title,
        Boolean isDefault,
        String fieldKey,
        BlockType type,
        BlockProperties properties,
        Integer position
        ) {

}
