package org.example.note.application.message.payload;

import java.util.UUID;

import org.example.note.application.dto.ProjectDto;
import org.example.note.domain.enums.ObjectType;

// 프로젝트 관련 페이로드
public record ProjectPayload(
        UUID projectId,
        String title,
        String description
        ) implements MessagePayload {

    @Override
    public ObjectType getObjectType() {
        return ObjectType.PROJECT;
    }

    // 필요한 팩토리 메서드
    public static ProjectPayload fromDto(ProjectDto dto) {
        return new ProjectPayload(dto.projectId(), dto.title(), dto.description());
    }
}
