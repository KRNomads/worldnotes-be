package org.example.note.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.common.exception.ErrorCode;
import org.example.note.adapter.out.repository.ProjectJpaRepository;
import org.example.note.application.dto.ProjectDto;
import org.example.note.domain.entity.Project;
import org.example.note.domain.exception.ProjectException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final NoteService noteService;
    private final ProjectJpaRepository projectJpaRepository;

    // === 조회 ===
    @Transactional(readOnly = true)
    public ProjectDto findById(UUID id) {
        Project project = projectJpaRepository.findById(id)
                .orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND, id));
        return ProjectDto.from(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> findByUserId(UUID userId) {
        return projectJpaRepository.findAll().stream()
                .filter(p -> p.getUserId().equals(userId)) // 성능 개선 필요
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

        // 디폴트 basic info 생성
        noteService.createDefaultNoteFor(project);

        return ProjectDto.from(project);
    }

    // === 업데이트 ===
    @Transactional
    public ProjectDto update(UUID id, String name, String description) {
        Project project = projectJpaRepository.findById(id)
                .orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND, id));

        project.update(name, description);

        return ProjectDto.from(project);
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
