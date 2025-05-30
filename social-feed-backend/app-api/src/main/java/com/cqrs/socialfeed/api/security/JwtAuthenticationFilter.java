package com.cqrs.socialfeed.api.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Base64;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);

        // 1. 토큰 존재 & 유효한 경우
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String tokenType = getTokenTypeFromToken(token);

            // Refresh Token을 인증 용도로 사용하는 것을 차단
            if ("rf".equals(tokenType)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Refresh Token cannot be used for authentication");
                return;
            }

            String username = jwtTokenProvider.getUsernameFromToken(token);

            // UserDetailsService를 사용할 수도 있음
            UserDetails userDetails = jwtTokenProvider.getUserDetails(username);

            // 2. 인증 정보 생성 & 컨텍스트에 저장
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
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
}
