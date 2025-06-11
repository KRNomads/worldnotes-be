package org.example.note.domain.template;

import java.util.List;

import org.example.note.domain.enums.BlockType;
import org.example.note.domain.enums.NoteType;
import org.example.note.domain.property.TextBlockProperties;

public class NoteBlockTemplates {

    public static List<BlockTemplate> getTemplates(NoteType noteType) {
        return switch (noteType) {
            case CHARACTER ->
                List.of(
                new BlockTemplate("나이", "age", BlockType.TEXT, new TextBlockProperties(""), 0),
                new BlockTemplate("종족", "tribe", BlockType.TEXT, new TextBlockProperties(""), 0),
                new BlockTemplate("성격", "personality", BlockType.TEXT, new TextBlockProperties(""), 0),
                new BlockTemplate("성별", "gender", BlockType.TEXT, new TextBlockProperties(""), 0)
                );
            case DETAILS ->
                List.of( // new BlockTemplate("세계관 설정", null, BlockType.TEXT, new TextBlockProperties(""), 0)
                );
            case PLACE ->
                List.of( // new BlockTemplate("세계관 설정", null, BlockType.TEXT, new TextBlockProperties(""), 0)
                );
            case EVENT ->
                List.of( // new BlockTemplate("세계관 설정", null, BlockType.TEXT, new TextBlockProperties(""), 0)
                );
            default ->
                throw new IllegalArgumentException("Invalid noteType: " + noteType);
        };
    }

}
