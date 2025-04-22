package org.example.note.application.message;

import java.util.Map;
import java.util.UUID;

import org.example.note.domain.enums.BlockType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 블록 업데이트 페이로드
 */
// 수정할 필드만 받는 방법 고려
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockUpdatePayload {

    private UUID noteId;
    private Long blockId;
    private String title;
    private BlockType type;
    private Map<String, Object> content;
    private Integer position;
    // private long timestamp; // 충돌 해결에 사용될 타임스탬프
}
