package org.example.note.domain.template;

import org.example.note.domain.enums.NoteType;
import org.example.note.domain.property.TextBlockProperties;
import org.example.note.domain.enums.BlockType;

import java.util.List;

public class NoteBlockTemplates {

    public static List<BlockTemplate> getTemplates(NoteType noteType) {
        return switch (noteType) {
            case BASIC_INFO ->
                List.of(
                new BlockTemplate("작품 제목", true, "title", BlockType.TEXT, new TextBlockProperties(""), 0),
                new BlockTemplate("장르", true, "genre", BlockType.TEXT, new TextBlockProperties(""), 0)
                );
            case CHARACTER ->
                List.of(
                new BlockTemplate("이름", true, "name", BlockType.TEXT, new TextBlockProperties(""), 0),
                new BlockTemplate("설명", true, "decription", BlockType.TEXT, new TextBlockProperties(""), 0),
                new BlockTemplate("관계", true, "relationship", BlockType.TEXT, new TextBlockProperties(""), 0)
                );
            case DETAILS ->
                List.of(
                new BlockTemplate("세계관 설정", false, null, BlockType.TEXT, new TextBlockProperties(""), 0)
                );
        };
    }

}
