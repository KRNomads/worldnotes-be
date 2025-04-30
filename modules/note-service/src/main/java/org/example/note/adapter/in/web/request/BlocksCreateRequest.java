package org.example.note.adapter.in.web.request;

import java.util.List;

import org.example.note.application.dto.BlockCreateParam;

public record BlocksCreateRequest(
        List<BlockCreateRequest> blocks
        ) {

    public List<BlockCreateParam> toParams() {
        return blocks.stream()
                .map(BlockCreateRequest::toParam)
                .toList();
    }
}
