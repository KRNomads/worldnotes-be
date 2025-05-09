package org.example.note.application;

import java.util.UUID;

import org.example.note.adapter.out.repository.ProjectJpaRepository;
import org.example.note.domain.entity.Project;
import org.example.note.domain.enums.MemberRole;
import org.example.note.domain.exception.ProjectException;
import org.example.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectPermissionService {

    private final ProjectJpaRepository projectJpaRepository;

    /**
     * OWNER 권한을 가진 경우에만 프로젝트 반환
     */
    public Project getProjectIfOwner(UUID userId, UUID projectId) {
        Project project = projectJpaRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND, projectId));

        boolean isOwner = project.getMembers().stream()
                .anyMatch(member -> member.getUserId().equals(userId)
                && member.getRole() == MemberRole.OWNER);

        if (!isOwner) {
            throw new ProjectException(ErrorCode.PROJECT_FORBIDDEN, projectId);
        }

        return project;
    }

    /**
     * 접근 권한(OWNER/EDITOR/VIEWER 중 하나라도 있는 경우)이 있는 경우에만 프로젝트 반환
     */
    public Project getProjectIfMember(UUID userId, UUID projectId) {
        Project project = projectJpaRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND, projectId));

        boolean isMember = project.getMembers().stream()
                .anyMatch(member -> member.getUserId().equals(userId));

        if (!isMember) {
            throw new ProjectException(ErrorCode.PROJECT_FORBIDDEN, projectId);
        }

        return project;
    }

    public void checkIsOwner(UUID userId, UUID projectId) {
        Project project = projectJpaRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND, projectId));

        boolean isOwner = project.getMembers().stream()
                .anyMatch(m -> m.getUserId().equals(userId) && m.getRole() == MemberRole.OWNER);

        if (!isOwner) {
            throw new ProjectException(ErrorCode.PROJECT_FORBIDDEN, projectId);
        }
    }
}
