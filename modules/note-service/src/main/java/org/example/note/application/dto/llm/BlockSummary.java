package org.example.note.application.dto.llm;

import java.util.Map;

import org.example.note.application.dto.BlockDto;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.property.ImageBlockProperties;
import org.example.note.domain.property.TextBlockProperties;

public record BlockSummary(
        String title,
        BlockType type,
        Object content
        ) {

    public static BlockSummary from(BlockDto dto) {
        Object content = switch (dto.type()) {
            case TEXT ->
                ((TextBlockProperties) dto.properties()).getValue();
            case IMAGE ->
                Map.of(
                "url", ((ImageBlockProperties) dto.properties()).getUrl(),
                "caption", ((ImageBlockProperties) dto.properties()).getCaption()
                );
            default ->
                throw new IllegalArgumentException("Unsupported BlockType: " + dto.type());
        };

        return new BlockSummary(dto.title(), dto.type(), content);
    }
}
