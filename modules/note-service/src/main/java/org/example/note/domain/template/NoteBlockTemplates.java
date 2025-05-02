package org.example.note.domain.template;

import java.util.List;

import org.example.note.domain.enums.BlockType;
import org.example.note.domain.enums.NoteType;
import org.example.note.domain.property.TextBlockProperties;

public class NoteBlockTemplates {

    public static List<BlockTemplate> getTemplates(NoteType noteType) {
        return switch (noteType) {
            case BASIC_INFO ->
                List.of(
                new BlockTemplate("작품 제목", true, "title", BlockType.TEXT, new TextBlockProperties(""), 0),
                new BlockTemplate("장르", true, "genre", BlockType.TEXT, new TextBlockProperties(""), 0),
                new BlockTemplate("주요 내용", true, "keycontent", BlockType.TEXT, new TextBlockProperties(""), 0)
                );
            case CHARACTER ->
                List.of(
                new BlockTemplate("나이", true, "age", BlockType.TEXT, new TextBlockProperties(""), 0),
                new BlockTemplate("종족", true, "tribe", BlockType.TEXT, new TextBlockProperties(""), 0)
                );
            case DETAILS ->
                List.of( // new BlockTemplate("세계관 설정", false, null, BlockType.TEXT, new TextBlockProperties(""), 0)
                );
        };
    }

}
