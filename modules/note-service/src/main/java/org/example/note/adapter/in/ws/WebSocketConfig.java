package org.example.note.adapter.in.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트가 구독할 주제 prefix 설정 
        // /topic/project/{projectId}, /topic/note/{noteId}, /topic/block/{blockId} 형태로 사용
        config.enableSimpleBroker("/topic");

        // 클라이언트가 메시지를 보낼 때 사용할 prefix
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 연결 엔드포인트 설정
        registry.addEndpoint("/ws/projects")
                .setAllowedOrigins("*") // 프로덕션에서는 구체적인 출처를 지정하는 것이 좋습니다
                .withSockJS(); // SockJS 지원 추가
    }

}
