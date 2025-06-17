package org.example.note.application.dto;

public record NoteUpdateParam(
        String title,
        String subTitle,
        String summary,
        String imgUrl,
        String color) {

}
