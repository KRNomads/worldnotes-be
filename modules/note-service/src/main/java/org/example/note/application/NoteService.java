package org.example.note.application;

import java.util.List;
import java.util.UUID;

import org.example.common.exception.ErrorCode;
import org.example.note.adapter.out.repository.NoteJpaRepository;
import org.example.note.adapter.out.repository.ProjectJpaRepository;
import org.example.note.application.dto.NoteDto;
import org.example.note.domain.entity.Note;
import org.example.note.domain.entity.Project;
import org.example.note.domain.enums.NoteType;
import org.example.note.domain.exception.NoteException;
import org.example.note.domain.exception.ProjectException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoteService {

    private final NoteJpaRepository noteJpaRepository;
    private final ProjectJpaRepository projectJpaRepository;
    private final BlockService blockService;
    private final ProjectPermissionService projectPermissionService;
    private final NotePermissionService notePermissionService;

    // === 조회 ===
    @Transactional(readOnly = true)
    public NoteDto findById(UUID userId, UUID id) {
        Note note = notePermissionService.getNoteIfOwner(userId, id);
        return NoteDto.from(note);
    }

    @Transactional(readOnly = true)
    public List<NoteDto> findByProjectId(UUID userId, UUID projectId) {
        projectPermissionService.checkIsOwner(userId, projectId);
        List<Note> notes = noteJpaRepository.findByProjectId(projectId);
        return notes.stream().map(NoteDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<NoteDto> findByProjectIdAndType(UUID userId, UUID projectId, NoteType type) {
        projectPermissionService.checkIsOwner(userId, projectId);
        List<Note> notes = noteJpaRepository.findByProjectIdAndType(projectId, type);
        return notes.stream().map(NoteDto::from).toList();
    }

    @Transactional(readOnly = true)
    public NoteDto findBasicInfoByProjectId(UUID projectId) {
        // 권한체크 필요 x
        Note note = noteJpaRepository.findByProjectIdAndType(projectId, NoteType.BASIC_INFO)
                .stream().findFirst()
                .orElseThrow(() -> new NoteException(ErrorCode.NOTE_NOT_FOUND, projectId));

        return NoteDto.from(note);
    }

    // === 생성 ===
    @Transactional
    public NoteDto create(UUID userId, UUID projectId, String title, NoteType type) {
        Project project = projectPermissionService.getProjectIfOwner(userId, projectId);

        if (type == NoteType.BASIC_INFO) {
            throw new IllegalArgumentException("BASIC_INFO 타입의 노트는 생성할 수 없습니다.");
        }

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
        Integer maxPosition = noteJpaRepository.findMaxPositionByProjectIdAndType(projectId, type).orElse(0);
        Integer newPosition = maxPosition + 100;

        Note note = Note.create(
                project,
                finalTitle,
                type,
                newPosition);

        noteJpaRepository.save(note);

        // 템플릿 기반 블록 생성
        blockService.createDefaultBlocksFor(note);

        return NoteDto.from(note);
    }

    public void createDefaultNoteFor(Project project) {
        // 권한체크 필요 x
        Note basicInfoNote = Note.create(
                project,
                "기본 정보",
                NoteType.BASIC_INFO,
                0);

        noteJpaRepository.save(basicInfoNote);

        blockService.createDefaultBlocksFor(basicInfoNote);
    }

    // === 업데이트 ===
    @Transactional
    public NoteDto update(UUID userId, UUID id, String title) {
        Note note = notePermissionService.getNoteIfOwner(userId, id);

        note.update(title);
        return NoteDto.from(note);
    }

    // position 업데이트 로직
    // === 삭제 ===
    @Transactional
    public void delete(UUID userId, UUID id) {
        Note note = notePermissionService.getNoteIfOwner(userId, id);
        noteJpaRepository.delete(note);
    }

}
