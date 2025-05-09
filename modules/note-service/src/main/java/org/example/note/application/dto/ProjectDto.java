package org.example.note.application.dto;

import java.util.UUID;

import org.example.note.domain.entity.Project;

public record ProjectDto(
        UUID projectId,
        String title,
        String description) implements NoteObjectDto {

    public static ProjectDto from(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getTitle(),
                project.getDescription()
        );
    }
}
