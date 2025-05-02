package org.example.note.adapter.in.web.request.llm;

import java.util.Map;
import java.util.UUID;

public record NewDetailsRequest(
        UUID projectId,
        String noteTitle,
        Map<String, String> extraFields
        ) {

}
