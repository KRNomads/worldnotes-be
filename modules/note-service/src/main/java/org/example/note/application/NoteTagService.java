package org.example.note.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.note.adapter.out.repository.NoteTagJpaRepository;
import org.example.note.adapter.out.repository.TagJpaRepository;
import org.example.note.application.dto.NoteDto;
import org.example.note.application.dto.TagDto;
import org.example.note.domain.entity.Note;
import org.example.note.domain.entity.NoteTag;
import org.example.note.domain.entity.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * NoteTagService: 노트 중 태그 관련 비즈니스 로직 처리
 */
@RequiredArgsConstructor
@Service
public class NoteTagService {

    private final TagJpaRepository tagRepository;
    private final NoteTagJpaRepository noteTagRepository;

    private final NotePermissionService notePermissionService;

    // === 조회 ===
    /**
     * 노트의 태그 목록 조회
     */
    @Transactional(readOnly = true)
    public List<TagDto> findTagsByNote(UUID userId, UUID noteId) {
        Note note = notePermissionService.getNoteIfOwner(userId, noteId);

        return note.getNoteTags().stream()
                .map(noteTag -> TagDto.fromEntity(noteTag.getTag()))
                .collect(Collectors.toList());
    }

    /**
     * 태그가 달린 노트 목록 조회
     */
    @Transactional(readOnly = true)
    public List<NoteDto> findNotesByTag(UUID projectId, UUID tagId) {
        Tag tag = tagRepository.findByIdAndProjectId(tagId, projectId)
                .orElseThrow(() -> new RuntimeException("태그를 찾을 수 없습니다."));

        // (권한 검증은 NotePermissionService 등에서 처리 가능!)
        return tag.getNoteTags().stream()
                .map(noteTag -> NoteDto.fromEntity(noteTag.getNote()))
                .collect(Collectors.toList());
    }

    // === 생성 ===
    /**
     * 노트에 태그 추가
     */
    @Transactional
    public void addTagToNote(UUID userId, UUID noteId, UUID tagId) {
        // 1) 노트 조회 (권한 검증)
        Note note = notePermissionService.getNoteIfOwner(userId, noteId);

        // 2) 태그 조회 (프로젝트 검증)
        Tag tag = tagRepository.findByIdAndProjectId(tagId, note.getProject().getId())
                .orElseThrow(() -> new RuntimeException("태그를 찾을 수 없습니다."));

        // 3) 이미 연결되어 있는지 확인
        boolean exists = note.getNoteTags().stream()
                .anyMatch(nt -> nt.getTag().equals(tag));

        if (!exists) {
            NoteTag noteTag = new NoteTag(note, tag);
            note.getNoteTags().add(noteTag);
            tag.getNoteTags().add(noteTag);
        }
    }

    // === 삭제 ===
    /**
     * 노트에서 태그 제거
     */
    @Transactional
    public void removeTagFromNote(UUID userId, UUID noteId, UUID tagId) {
        // 1) 노트 조회 (권한 검증)
        Note note = notePermissionService.getNoteIfOwner(userId, noteId);

        // 2) 태그 조회 (프로젝트 검증)
        Tag tag = tagRepository.findByIdAndProjectId(tagId, note.getProject().getId())
                .orElseThrow(() -> new RuntimeException("태그를 찾을 수 없습니다."));

        // 3) 연결 제거 (양방향 정리)
        note.getNoteTags().removeIf(nt -> nt.getTag().equals(tag));
        tag.getNoteTags().removeIf(nt -> nt.getNote().equals(note));

        // 4) 실제 연결 테이블에서 삭제
        noteTagRepository.deleteByNoteAndTag(note, tag);
    }

}
