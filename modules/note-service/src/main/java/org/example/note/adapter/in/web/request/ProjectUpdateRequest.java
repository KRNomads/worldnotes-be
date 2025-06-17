package org.example.note.adapter.in.web.request;

import org.example.note.application.dto.ProjectUpdateParam;

public record ProjectUpdateRequest(
        String title,
        String overview,
        String synopsis,
        String genre) {

    public ProjectUpdateParam toParam() {
        return new ProjectUpdateParam(title, overview, synopsis, genre);
    }

}
