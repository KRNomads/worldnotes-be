package org.example.note.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.common.exception.ErrorCode;
import org.example.note.adapter.out.ProjectJpaRepository;
import org.example.note.application.dto.ProjectDto;
import org.example.note.domain.entity.Project;
import org.example.note.domain.exception.ProjectException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectJpaRepository projectJpaRepository;

    // === 생성 ===
    @Transactional
    public ProjectDto create(UUID userId, String name, String description) {
        Project project = Project.create(
                userId,
                name,
                description);
        return ProjectDto.fromEntity(projectJpaRepository.save(project));
    }

    // === 조회 ===
    @Transactional(readOnly = true)
    public ProjectDto findById(UUID id) {
        Project project = projectJpaRepository.findById(id)
                .orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND, id));
        return ProjectDto.fromEntity(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> findByUserId(UUID userId) {
        return projectJpaRepository.findAll().stream()
                .filter(p -> p.getUserId().equals(userId)) // 성능 개선 필요
                .map(ProjectDto::fromEntity)
                .collect(Collectors.toList());
    }

    // === 업데이트 ===
    @Transactional
    public ProjectDto update(UUID id, String name, String description) {
        Project project = projectJpaRepository.findById(id)
                .orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND, id));

        project.update(name, description);

        return ProjectDto.fromEntity(project);
    }

    // === 삭제 ===
    @Transactional
    public void delete(UUID id) {
        if (!projectJpaRepository.existsById(id)) {
            throw new ProjectException(ErrorCode.PROJECT_NOT_FOUND, id);
        }
        projectJpaRepository.deleteById(id);
    }
}
