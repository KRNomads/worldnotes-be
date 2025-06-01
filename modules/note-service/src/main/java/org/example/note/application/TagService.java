package org.example.note.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.note.adapter.out.repository.TagJpaRepository;
import org.example.note.application.dto.TagDto;
import org.example.note.domain.entity.Project;
import org.example.note.domain.entity.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * TagService: 태그 관련 CRUD 비즈니스 로직 처리
 */
@RequiredArgsConstructor
@Service
public class TagService {

    private final ProjectPermissionService projectPermissionService;
    private final TagJpaRepository tagRepository;

    // ==== 조회 ====
    /**
     * 단일 태그 조회
     */
    @Transactional(readOnly = true)
    public TagDto getTag(UUID tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("태그를 찾을 수 없습니다."));
        return TagDto.fromEntity(tag);
    }

    /**
     * 모든 태그 조회
     */
    @Transactional(readOnly = true)
    public List<TagDto> findAllTagsByProject(UUID projectId) {
        return tagRepository.findByProjectId(projectId).stream()
                .map(TagDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ==== 생성 ====
    /**
     * 새로운 태그 생성 (중복 체크)
     */
    @Transactional
    public TagDto createTag(UUID userId, UUID projectId, String tagName, String color) {
        Project project = projectPermissionService.getProjectIfOwner(userId, projectId);

        // 같은 프로젝트 안에서 중복 방지
        boolean exists = tagRepository.existsByNameAndProjectId(tagName, projectId);
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 태그입니다: " + tagName);
        }

        Tag newTag = Tag.create(project, tagName, color);

        Tag createdTag = tagRepository.save(newTag);
        return TagDto.fromEntity(createdTag);
    }

    // === 업데이트 ===
    /**
     * 태그 업데이트
     */
    @Transactional
    public TagDto updateTag(UUID projectId, UUID tagId, String tagName, String color) {
        Tag tag = tagRepository.findByIdAndProjectId(tagId, projectId)
                .orElseThrow(() -> new RuntimeException("태그를 찾을 수 없습니다."));

        if (tagName != null) {
            tag.updateName(tagName);
        }

        if (color != null) {
            tag.updateColor(color);
        }

        Tag updatedTag = tagRepository.save(tag);
        return TagDto.fromEntity(updatedTag);
    }

    // ==== 삭제 ====
    /**
     * 태그 삭제
     */
    @Transactional
    public void deleteTag(UUID projectId, UUID tagId) {
        Tag tag = tagRepository.findByIdAndProjectId(tagId, projectId)
                .orElseThrow(() -> new RuntimeException("태그를 찾을 수 없습니다."));
        tagRepository.delete(tag);
    }
}
