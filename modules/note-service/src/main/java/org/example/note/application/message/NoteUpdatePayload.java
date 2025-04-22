package org.example.note.application.message;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 노트 업데이트 페이로드
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteUpdatePayload {

    private UUID projectId;
    private UUID noteId;
    private String title;
    private Integer position;
}
