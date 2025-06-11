package org.example.note.application;

import java.util.List;
import java.util.UUID;

import org.example.common.exception.ErrorCode;
import org.example.note.adapter.out.repository.NoteJpaRepository;
import org.example.note.application.dto.NoteDto;
import org.example.note.domain.entity.Note;
import org.example.note.domain.entity.Project;
import org.example.note.domain.enums.NoteType;
import org.example.note.domain.exception.NoteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * NoteService: 노트 관련 CRUD 비즈니스 로직 처리
 */
@RequiredArgsConstructor
@Service
public class NoteService {

    private final NoteJpaRepository noteRepository;

    private final BlockService blockService;
    private final ProjectPermissionService projectPermissionService;
    private final NotePermissionService notePermissionService;

    // === 조회 ===
    @Transactional(readOnly = true)
    public NoteDto findById(UUID userId, UUID id) {
        Note note = notePermissionService.getNoteIfOwner(userId, id);
        return NoteDto.fromEntity(note);
    }

    @Transactional(readOnly = true)
    public List<NoteDto> findByProjectId(UUID userId, UUID projectId) {
        projectPermissionService.checkIsOwner(userId, projectId);
        List<Note> notes = noteRepository.findByProjectId(projectId);
        return notes.stream().map(NoteDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<NoteDto> findByProjectIdAndType(UUID userId, UUID projectId, NoteType type) {
        projectPermissionService.checkIsOwner(userId, projectId);
        List<Note> notes = noteRepository.findByProjectIdAndType(projectId, type);
        return notes.stream().map(NoteDto::fromEntity).toList();
    }

    // === 생성 ===
    @Transactional
    public NoteDto create(UUID userId, UUID projectId, String title, NoteType type) {
        Project project = projectPermissionService.getProjectIfOwner(userId, projectId);

        String finalTitle = (title == null || title.trim().isEmpty())
                ? switch (type) {
            case CHARACTER ->
                "새 등장인물";
            case DETAILS ->
                "새 설정";
            default ->
                throw new IllegalStateException("알 수 없는 노트 타입입니다.");
        }
                : title.trim();

        // 위치 설정
        Integer maxPosition = noteRepository.findMaxPositionByProjectIdAndType(projectId, type).orElse(0);
        Integer newPosition = maxPosition + 100;

        Note note = Note.create(
                project,
                finalTitle,
                type,
                newPosition);

        noteRepository.save(note);

        // 템플릿 기반 블록 생성
        blockService.createDefaultBlocksFor(note);

        return NoteDto.fromEntity(note);
    }

    // === 업데이트 ===
    @Transactional
    public NoteDto update(UUID userId, UUID id, String title) {
        Note note = notePermissionService.getNoteIfOwner(userId, id);

        note.update(title);
        return NoteDto.fromEntity(note);
    }

    // position 업데이트 로직
    // === 삭제 ===
    @Transactional
    public void delete(UUID userId, UUID id) {
        Note note = notePermissionService.getNoteIfOwner(userId, id);
        noteRepository.delete(note);
    }

}
