package org.example.note.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.note.domain.entity.Note;
import org.example.note.domain.enums.NoteType;

public record NoteDto(
        UUID id,
        UUID projectId,
        String title,
        String subTitle,
        String summary,
        String imgUrl,
        String color,
        NoteType type,
        Integer position,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static NoteDto fromEntity(Note note) {
        return new NoteDto(
                note.getId(),
                note.getProject().getId(),
                note.getTitle(),
                note.getSubTitle(),
                note.getSummary(),
                note.getImgUrl(),
                note.getColor(),
                note.getType(),
                note.getPosition(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}
