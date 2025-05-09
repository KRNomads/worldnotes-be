package org.example.note.application;

import java.util.List;
import java.util.UUID;

import org.example.common.exception.ErrorCode;
import org.example.note.adapter.out.repository.NoteJpaRepository;
import org.example.note.domain.entity.Note;
import org.example.note.domain.enums.MemberRole;
import org.example.note.domain.exception.NoteException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotePermissionService {

    private final NoteJpaRepository noteJpaRepository;

    public Note getNoteIfOwner(UUID userId, UUID noteId) {
        return noteJpaRepository.findNoteWithPermission(userId, noteId, List.of(MemberRole.OWNER))
                .orElseThrow(() -> new NoteException(ErrorCode.NOTE_FORBIDDEN, noteId));
    }

    public void checkIsOwner(UUID userId, UUID noteId) {
        noteJpaRepository.findNoteWithPermission(userId, noteId, List.of(MemberRole.OWNER))
                .orElseThrow(() -> new NoteException(ErrorCode.NOTE_FORBIDDEN, noteId));
    }

}
