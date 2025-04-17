package org.example.note.application.dto;

import java.util.UUID;

import org.example.note.domain.entity.Project;

public record ProjectDto(
        UUID id,
        UUID userId,
        String name,
        String description) {

    public static ProjectDto fromEntity(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getUserId(),
                project.getName(),
                project.getDescription()
        );
    }
}
