package com.cqrs.socialfeed.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompInterceptor stompInterceptor;

    public WebSocketConfig(StompInterceptor stompInterceptor) {
        this.stompInterceptor = stompInterceptor;
    }

    // WebSocket 연결 엔드포인트
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws").setAllowedOriginPatterns("*")
//                .withSockJS()
//                .setHeartbeatTime(30000) // 클라이언트와 서버 간의 heartbeat 타임아웃 설정 (30초)
//                .setDisconnectDelay(30000);  // WebSocket이 닫히기 전에 대기할 최대 시간 설정 (30초);
        registry.addEndpoint("/scf").setAllowedOrigins("http://localhost:8080");
        registry.addEndpoint("/scf").setAllowedOrigins("http://localhost:8080").withSockJS();
    }

    /*
    *   | 역할               | 경로                              |
        | ---------------- | ------------------------------- |
        | 클라이언트가 메시지 보낼 때  | `/app/notifications/reconnect`  |
        | 서버에서 클라이언트로 보낼 때 | `/topic/notifications/{userId}` |
    * */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 구독 대상 prefix  // 구독 대상
        config.setApplicationDestinationPrefixes("/app"); // 발신 대상 prefix  // 클라 → 서버 발신
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompInterceptor);
    }

}
