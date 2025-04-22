package org.example.note.application.message;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 프로젝트 업데이트 페이로드
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdatePayload {

    private UUID projectId;
    private String title;
    private String description;
}
