package org.example.note.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.note.domain.entity.Project;

public record ProjectDto(
        UUID id,
        String title,
        String overview,
        String synopsis,
        String genre,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static ProjectDto from(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getTitle(),
                project.getOverview(),
                project.getSynopsis(),
                project.getGenre(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
