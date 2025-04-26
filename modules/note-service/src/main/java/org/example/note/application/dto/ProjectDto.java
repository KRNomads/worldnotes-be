package org.example.note.application.dto;

import java.util.UUID;

import org.example.note.domain.entity.Project;

public record ProjectDto(
        UUID projectId,
        UUID userId,
        String title,
        String description) implements NoteObjectDto {

    public static ProjectDto from(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getUserId(),
                project.getTitle(),
                project.getDescription()
        );
    }
}
