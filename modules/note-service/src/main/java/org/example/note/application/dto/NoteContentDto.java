package org.example.note.application.dto;

import java.util.List;
import java.util.UUID;

import org.example.note.domain.enums.NoteType;

public record NoteContentDto(
        UUID noteId,
        String noteTitle,
        NoteType type,
        List<BlockSummary> blocks
        ) {

}
