package org.example.note.application.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.example.note.domain.enums.NoteType;

public record ProjectMetaDto(
        UUID projectId,
        String projectTitle,
        Map<NoteType, List<NoteSummary>> meta
        ) {

}
