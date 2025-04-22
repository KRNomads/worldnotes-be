package org.example.note.domain.template;

import org.example.note.domain.enums.NoteType;
import org.example.note.domain.enums.BlockType;

import java.util.List;
import java.util.Map;

public class NoteBlockTemplates {

    public static List<BlockTemplate> getTemplates(NoteType noteType) {
        return switch (noteType) {
            case BASIC_INFO ->
                List.of(
                new BlockTemplate("이름", true, BlockType.TEXT, Map.of("text", ""), 0),
                new BlockTemplate("설명", true, BlockType.TEXT, Map.of("text", ""), 1)
                );
            case CHARACTER ->
                List.of(
                new BlockTemplate("이름", true, BlockType.TEXT, Map.of("text", ""), 0),
                new BlockTemplate("설명", true, BlockType.TEXT, Map.of("text", ""), 1),
                new BlockTemplate("관계", true, BlockType.TEXT, Map.of("items", List.of()), 2)
                );
            case DETAILS ->
                List.of(
                new BlockTemplate("세계관 설정", true, BlockType.TEXT, Map.of("text", ""), 0)
                );
        };
    }

}
