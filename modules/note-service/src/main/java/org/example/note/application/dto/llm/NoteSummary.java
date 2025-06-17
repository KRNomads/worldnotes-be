package org.example.note.application.dto.llm;

import java.util.UUID;

import org.example.note.application.dto.NoteDto;

public record NoteSummary(
        UUID noteId,
        String title,
        Integer position
        ) {

    public static NoteSummary from(NoteDto dto) {
        return new NoteSummary(dto.id(), dto.title(), dto.position());
    }
}
