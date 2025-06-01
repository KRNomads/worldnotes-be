package org.example.note.application.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.note.domain.entity.NoteGroup;
import org.example.note.domain.entity.NoteGroupEntry;
import org.example.note.domain.enums.GroupType;

public record NoteGroupDto(
        Long id,
        String title,
        GroupType type,
        List<EntryDto> entries
        ) {

    // NoteGroup -> NoteGroupDto 변환
    public static NoteGroupDto from(NoteGroup group) {
        List<EntryDto> entryDtos = group.getEntries().stream()
                .map(EntryDto::from)
                .collect(Collectors.toList());

        return new NoteGroupDto(
                group.getId(),
                group.getTitle(),
                group.getType(),
                entryDtos
        );
    }

    public record EntryDto(
            Long id,
            UUID noteId,
            Integer positionX,
            Integer positionY
            ) {

        public static EntryDto from(NoteGroupEntry entry) {
            return new EntryDto(
                    entry.getId(),
                    entry.getNote().getId(),
                    entry.getPositionX(),
                    entry.getPositionY()
            );
        }
    }

}
