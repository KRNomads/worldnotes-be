package org.example.note.application.message;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 블록 삭제 페이로드
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockDeletePayload {

    private UUID noteId;
    private Long blockId;
}
