package org.example.note.application.message;

import lombok.Data;

@Data
public class WebSocketMessage {

    private String type;
    private String userId;
    private Object payload;

    public static WebSocketMessage of(String type, String userId, Object payload) {
        WebSocketMessage msg = new WebSocketMessage();
        msg.setType(type);
        msg.setUserId(userId);
        msg.setPayload(payload);
        return msg;
    }
}
