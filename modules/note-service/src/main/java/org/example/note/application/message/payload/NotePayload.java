package org.example.note.application.message.payload;

import java.util.UUID;

import org.example.note.application.dto.NoteDto;
import org.example.note.domain.enums.NoteType;
import org.example.note.domain.enums.ObjectType;

// 노트 관련 페이로드
public record NotePayload(
        UUID noteId,
        UUID projectId,
        String title,
        NoteType type,
        Integer position
        ) implements MessagePayload {

    @Override
    public ObjectType getObjectType() {
        return ObjectType.NOTE;
    }

    // 필요한 팩토리 메서드
    public static NotePayload fromDto(NoteDto dto) {
        return new NotePayload(dto.noteId(), dto.projectId(), dto.title(), dto.type(), dto.position());
    }
}
