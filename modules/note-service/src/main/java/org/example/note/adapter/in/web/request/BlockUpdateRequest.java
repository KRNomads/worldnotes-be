package org.example.note.adapter.in.web.request;

import java.util.Map;

public record BlockUpdateRequest(
        Map<String, Object> updateFields) {

}
