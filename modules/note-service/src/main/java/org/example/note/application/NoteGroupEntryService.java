package org.example.note.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.note.adapter.out.repository.NoteGroupEntryJpaRepository;
import org.example.note.adapter.out.repository.NoteGroupJpaRepository;
import org.example.note.adapter.out.repository.NoteJpaRepository;
import org.example.note.application.dto.NoteGroupDto.EntryDto;
import org.example.note.domain.entity.Note;
import org.example.note.domain.entity.NoteGroup;
import org.example.note.domain.entity.NoteGroupEntry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoteGroupEntryService {

    private final NoteGroupJpaRepository noteGroupRepository;
    private final NoteJpaRepository noteRepository;
    private final NoteGroupEntryJpaRepository entryRepository;

    // === 조회 ===
    /**
     * 그룹 내 엔트리 목록 조회
     */
    @Transactional(readOnly = true)
    public List<EntryDto> getEntriesInGroup(Long groupId) {
        return entryRepository.findByGroupId(groupId)
                .stream()
                .map(EntryDto::from)
                .collect(Collectors.toList());
    }

    // === 생성 ===
    /**
     * 그룹에 노트 엔트리 추가
     */
    @Transactional
    public EntryDto addEntry(Long groupId, UUID noteId, Integer positionX, Integer positionY) {
        NoteGroup group = noteGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        // 중복 체크
        boolean exists = entryRepository.findByGroupId(groupId).stream()
                .anyMatch(entry -> entry.getNote().getId().equals(noteId));
        if (exists) {
            throw new IllegalArgumentException("Note already in this group");
        }

        NoteGroupEntry entry = new NoteGroupEntry();
        entry.setGroup(group);
        entry.setNote(note);
        entry.setPositionX(positionX);
        entry.setPositionY(positionY);

        NoteGroupEntry savedEntry = entryRepository.save(entry);
        return EntryDto.from(savedEntry);
    }

    // === 업데이트 ===
    /**
     * 엔트리 위치 수정
     */
    @Transactional
    public EntryDto updateEntryPosition(Long entryId, Integer positionX, Integer positionY) {
        NoteGroupEntry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("Entry not found"));

        entry.setPositionX(positionX);
        entry.setPositionY(positionY);

        return EntryDto.from(entry);
    }

    // === 삭제 ===
    /**
     * 그룹에서 노트 엔트리 삭제
     */
    @Transactional
    public void removeEntry(Long entryId) {
        entryRepository.deleteById(entryId);
    }

}
