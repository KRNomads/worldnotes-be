package org.example.note.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.note.adapter.out.repository.NoteGroupJpaRepository;
import org.example.note.application.dto.NoteGroupDto;
import org.example.note.domain.entity.NoteGroup;
import org.example.note.domain.entity.Project;
import org.example.note.domain.enums.GroupType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoteGroupService {

    private final NoteGroupJpaRepository noteGroupRepository;
    private final ProjectPermissionService projectPermissionService;

    // === 조회 ===
    /**
     * 특정 프로젝트의 모든 그룹 조회
     */
    @Transactional(readOnly = true)
    public List<NoteGroupDto> getGroupsByProject(UUID projectId) {
        return noteGroupRepository.findByProjectId(projectId)
                .stream()
                .map(NoteGroupDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 단일 그룹 조회
     */
    @Transactional(readOnly = true)
    public Optional<NoteGroupDto> getGroup(Long groupId) {
        return noteGroupRepository.findById(groupId)
                .map(NoteGroupDto::from);
    }

    // === 생성 ===
    /**
     * 그룹 생성
     */
    @Transactional
    public NoteGroupDto createGroup(UUID userId, UUID projectId, String title, GroupType type) {
        Project project = projectPermissionService.getProjectIfOwner(userId, projectId);

        NoteGroup group = new NoteGroup();
        group.setProject(project);
        group.updateTitle(title);
        group.updateType(type);

        NoteGroup savedGroup = noteGroupRepository.save(group);
        return NoteGroupDto.from(savedGroup);
    }

    // === 업데이트 ===
    /**
     * 그룹 수정
     */
    @Transactional
    public NoteGroupDto updateGroup(Long groupId, String newTitle) {
        NoteGroup group = noteGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (newTitle != null) {
            group.updateTitle(newTitle);
        }

        return NoteGroupDto.from(group);
    }

    // === 삭제 ===
    /**
     * 그룹 삭제
     */
    @Transactional
    public void deleteGroup(Long groupId) {
        noteGroupRepository.deleteById(groupId);
    }

}
