package org.example.note.application.message;

import java.util.Map;
import java.util.UUID;

import org.example.note.domain.enums.BlockType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 블록 생성 페이로드
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockCreatePayload {

    private UUID noteId;
    private Long blockId; // 생성된 블록 ID
    private String title;
    private boolean isDefault;
    private BlockType type;
    private Map<String, Object> content;
    private Integer position;
}
