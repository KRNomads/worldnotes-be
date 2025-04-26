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

    // === 생성 ===
    @Transactional
    public NoteDto create(UUID projectId, String title, NoteType type, Integer position) {
        Project project = projectJpaRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND, projectId));

        String finalTitle = (title == null || title.trim().isEmpty())
                ? switch (type) {
            case BASIC_INFO ->
                "기본 정보";
            case CHARACTER ->
                "새 등장인물";
            case DETAILS ->
                "새 설정";
        }
                : title.trim();

        // 위치 설정
        Integer newPosition = 0;

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

    // === 조회 ===
    @Transactional(readOnly = true)
    public NoteDto findById(UUID id) {
        Note note = noteJpaRepository.findById(id)
                .orElseThrow(() -> new NoteException(ErrorCode.NOTE_NOT_FOUND, id));
        return NoteDto.from(note);
    }

    @Transactional(readOnly = true)
    public List<NoteDto> findByProjectId(UUID projectId) {
        List<Note> notes = noteJpaRepository.findByProjectId(projectId);
        return notes.stream().map(NoteDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<NoteDto> findByProjectIdAndType(UUID projectId, NoteType type) {
        List<Note> notes = noteJpaRepository.findByProjectIdAndType(projectId, type);
        return notes.stream().map(NoteDto::from).toList();
    }

    // === 업데이트 ===
    @Transactional
    public NoteDto update(UUID id, String title, Integer position) {
        Note note = noteJpaRepository.findById(id)
                .orElseThrow(() -> new NoteException(ErrorCode.NOTE_NOT_FOUND, id));

        note.update(title, position);
        return NoteDto.from(note);
    }

    // === 삭제 ===
    @Transactional
    public void delete(UUID id) {
        if (!noteJpaRepository.existsById(id)) {
            throw new NoteException(ErrorCode.NOTE_NOT_FOUND, id);
        }
        noteJpaRepository.deleteById(id);
    }

}
