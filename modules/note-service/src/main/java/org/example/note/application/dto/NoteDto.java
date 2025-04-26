package org.example.note.application.dto;

import java.util.UUID;

import org.example.note.domain.entity.Note;
import org.example.note.domain.enums.NoteType;

public record NoteDto(
        UUID noteId,
        UUID projectId,
        String title,
        NoteType type,
        Integer position) implements NoteObjectDto {

    public static NoteDto from(Note note) {
        return new NoteDto(
                note.getId(),
                note.getProject().getId(),
                note.getTitle(),
                note.getType(),
                note.getPosition()
        );
    }
}
