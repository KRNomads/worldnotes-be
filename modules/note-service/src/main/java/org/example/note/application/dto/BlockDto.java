package org.example.note.application.dto;

import java.util.UUID;

import org.example.note.domain.entity.Block;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.property.BlockProperties;

public record BlockDto(
        Long blockId,
        UUID projectId,
        UUID noteId,
        String title,
        boolean isDefault,
        BlockType type,
        BlockProperties properties,
        Integer position) implements NoteObjectDto {

    public static BlockDto from(Block block) {
        return new BlockDto(
                block.getId(),
                block.getNote().getProject().getId(),
                block.getNote().getId(),
                block.getTitle(),
                block.isDefault(),
                block.getType(),
                block.getProperties(),
                block.getPosition()
        );
    }
}
