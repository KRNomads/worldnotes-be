package org.example.note.application.dto;

import java.util.UUID;

import org.example.note.domain.entity.Tag;

public record TagDto(
        UUID id,
        String name,
        String color
        ) {

    public static TagDto fromEntity(Tag tag) {
        return new TagDto(tag.getId(), tag.getName(), tag.getColor());
    }

}
