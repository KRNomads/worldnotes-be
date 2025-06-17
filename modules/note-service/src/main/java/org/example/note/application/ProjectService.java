package org.example.note.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.note.adapter.out.repository.ProjectJpaRepository;
import org.example.note.application.dto.ProjectDto;
import org.example.note.application.dto.ProjectUpdateParam;
import org.example.note.domain.entity.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectPermissionService projectPermissionService;
    private final ProjectJpaRepository projectJpaRepository;

    // === 조회 ===
    @Transactional(readOnly = true)
    public ProjectDto findById(UUID userId, UUID projectId) {
        Project project = projectPermissionService.getProjectIfOwner(userId, projectId);
        return ProjectDto.from(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> findByUserId(UUID userId) {
        List<Project> projects = projectJpaRepository.findProjectsOwnedByUser(userId);
        return projects.stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }

    // === 생성 ===
    @Transactional
    public ProjectDto create(UUID userId, String title, String description) {
        String finalTitle = (title == null || title.trim().isEmpty()) ? "새 프로젝트" : title.trim();
        String finalDescription = (description == null || description.trim().isEmpty()) ? "" : description.trim();

        Project project = Project.create(
                userId,
                finalTitle,
                finalDescription);

        projectJpaRepository.save(project);

        return ProjectDto.from(project);
    }

    // === 업데이트 ===
    @Transactional
    public ProjectDto update(UUID userId, UUID projectId, ProjectUpdateParam param) {
        Project project = projectPermissionService.getProjectIfOwner(userId, projectId);

        if (param.title() != null) {
            project.updateTitle(param.title());
        }

        if (param.overview() != null) {
            project.updateOverview(param.overview());
        }

        if (param.synopsis() != null) {
            project.updateSynopsis(param.synopsis());
        }

        if (param.genre() != null) {
            project.updateGenre(param.genre());
        }

        return ProjectDto.from(project);
    }

    // === 삭제 ===
    @Transactional
    public void delete(UUID userId, UUID projectId) {
        Project project = projectPermissionService.getProjectIfOwner(userId, projectId);
        projectJpaRepository.delete(project);
    }

}
