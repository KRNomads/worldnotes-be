package org.example.note.application.dto;

import java.util.Map;

import org.example.note.domain.entity.Block;
import org.example.note.domain.enums.BlockType;

public record BlockDto(
        Long id,
        // noteId 만?
        String title,
        boolean isDefault,
        BlockType type,
        Map<String, Object> content,
        Integer position) {

    public static BlockDto from(Block block) {
        return new BlockDto(
                block.getId(),
                block.getTitle(),
                block.isDefault(),
                block.getType(),
                block.getContent(),
                block.getPosition()
        );
    }
}
