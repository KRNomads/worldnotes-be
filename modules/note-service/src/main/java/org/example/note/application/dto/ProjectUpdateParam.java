package org.example.note.application.dto;

public record ProjectUpdateParam(
        String title,
        String overview,
        String synopsis,
        String genre
        ) {

}
