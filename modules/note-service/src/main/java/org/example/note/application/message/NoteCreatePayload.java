package org.example.note.application.message;

import java.util.UUID;

import org.example.note.domain.enums.NoteType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 노트 생성 페이로드
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteCreatePayload {

    private UUID projectId;
    private UUID noteId;
    private String title;
    private NoteType type;
    private Integer position;
}
