package org.example.note.adapter.in.web.request;

import org.example.note.application.dto.NoteUpdateParam;

public record NoteUpdateRequest(
        String title,
        String subTitle,
        String summary,
        String imgUrl,
        String color) {

    public NoteUpdateParam toParam() {
        return new NoteUpdateParam(title, subTitle, summary, imgUrl, color);
    }

}
