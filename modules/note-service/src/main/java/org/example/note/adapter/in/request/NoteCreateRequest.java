package org.example.note.adapter.in.request;

import java.util.UUID;

import org.example.note.domain.enums.NoteType;

public record NoteCreateRequest(
        UUID projectId,
        String title,
        NoteType type,
        Integer position) {

}
