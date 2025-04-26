package org.example.note.application.dto;

import java.util.Map;
import java.util.UUID;

import org.example.note.domain.entity.Block;
import org.example.note.domain.enums.BlockType;

public record BlockDto(
        Long blockId,
        UUID projectId,
        UUID noteId,
        String title,
        boolean isDefault,
        BlockType type,
        Map<String, Object> content,
        Integer position) implements NoteObjectDto {

    public static BlockDto from(Block block) {
        return new BlockDto(
                block.getId(),
                block.getNote().getProject().getId(),
                block.getNote().getId(),
                block.getTitle(),
                block.isDefault(),
                block.getType(),
                block.getContent(),
                block.getPosition()
        );
    }
}
