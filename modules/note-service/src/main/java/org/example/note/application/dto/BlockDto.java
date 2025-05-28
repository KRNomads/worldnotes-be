package org.example.note.application.dto;

import java.util.UUID;

import org.example.note.domain.entity.Block;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.property.BlockProperties;

public record BlockDto(
        Long blockId,
        UUID noteId,
        String title,
        String fieldKey,
        BlockType type,
        BlockProperties properties,
        Integer position) {

    public static BlockDto from(Block block) {
        return new BlockDto(
                block.getId(),
                block.getNote().getId(),
                block.getTitle(),
                block.getFieldKey(),
                block.getType(),
                block.getProperties(),
                block.getPosition()
        );
    }
}
