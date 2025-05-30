package com.cqrs.socialfeed.api.config;

import com.cqrs.socialfeed.api.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Objects;

@Component
public class StompInterceptor implements ChannelInterceptor {
    private final static Logger log = LoggerFactory.getLogger(StompInterceptor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int MAX_MESSAGE_SIZE = 1024 * 1024;  // 1MB

    private final JwtTokenProvider jwtTokenProvider;

    public StompInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        // 메시지 내용 크기 확인
        if (message.getPayload() instanceof String) {
            String payload = (String) message.getPayload();

            if (payload.length() > MAX_MESSAGE_SIZE) {
                log.error("Message size exceeds the maximum limit: {} bytes", payload.length());
                throw new AccessDeniedException("Message size exceeds the maximum allowed limit");
            }
        }

        String token = headerAccessor.getFirstNativeHeader("Authorization");
        StompCommand socketStompCommand = Objects.requireNonNull(headerAccessor.getCommand());

        switch (socketStompCommand) {
            case CONNECT:
            case SEND:
                if (token != null && token.startsWith("Bearer ")) {
                    String jwtToken = token.substring(7);

                    if (jwtTokenProvider.validateToken(jwtToken)) {
                        String tokenType = getTokenTypeFromToken(jwtToken);

                        // Access Token이 아닌 경우 연결 차단
                        if ("rf".equals(tokenType)) {
                            throw new AccessDeniedException("Refresh Token cannot be used for authentication");
                        }

                        String usernameFromToken = jwtTokenProvider.getUsernameFromToken(jwtToken);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                usernameFromToken, null, jwtTokenProvider.getUserDetails(usernameFromToken).getAuthorities());

                        // 인증 정보 설정
                        headerAccessor.setUser(authentication);

                    } else {
                        throw new AccessDeniedException("Invalid or expired authentication token");
                    }
                } else {
                    // Authorization 헤더가 없거나 JWT 인증 실패 시 연결 거부
                    throw new AccessDeniedException("Authorization header is missing or invalid");
                }

                break;
            default:
                break;
        }

        return message;
    }

    private String getTokenTypeFromToken(String token) {
        try {
            String[] tokenParts = token.split("\\.");
            String headerPart = tokenParts[0];

            String headerJson = new String(Base64.getDecoder().decode(headerPart));

            JsonNode headerNode = objectMapper.readTree(headerJson);

            return headerNode.has("token_type") ? headerNode.get("token_type").asText() : "unknown";
        } catch (Exception e) {
            throw new RuntimeException("JWT 헤더를 파싱하는 중 오류 발생", e);
        }
    }

    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();

        switch ((Objects.requireNonNull(accessor.getCommand()))) {
            case CONNECT:
                log.info("Connection: {}", sessionId);
                break;

            case DISCONNECT:
                log.info("Connection Closed: {}", sessionId);
                break;

            default:
                break;
        }
    }
}
