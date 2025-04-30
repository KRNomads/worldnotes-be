package org.example.note.application.dto;

import java.util.UUID;

public record NoteSummary(
        UUID noteId,
        String title,
        Integer position
        ) {

    public static NoteSummary from(NoteDto dto) {
        return new NoteSummary(dto.noteId(), dto.title(), dto.position());
    }
}
