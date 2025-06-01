package org.example.note.adapter.in.web.request;

import java.util.UUID;

public record NoteGroupEntryAddRequest(
        UUID noteId,
        Integer positionX,
        Integer positionY
        ) {

}
