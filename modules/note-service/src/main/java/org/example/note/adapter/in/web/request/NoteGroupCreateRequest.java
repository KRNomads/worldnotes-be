package org.example.note.adapter.in.web.request;

import java.util.UUID;

import org.example.note.domain.enums.GroupType;

public record NoteGroupCreateRequest(
        UUID projectId,
        String title,
        GroupType type
        ) {

}
