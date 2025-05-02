package org.example.note.adapter.in.web.request.llm;

import java.util.Map;
import java.util.UUID;

public record NewCharacterRequest(
        UUID projectId,
        String noteTitle,
        String age,
        String tribe,
        Map<String, String> extraFields
        ) {

}
