package com.cqrs.socialfeed.api.security;

import com.cqrs.socialfeed.command.auth.command.LoginTokenCommand;
import com.cqrs.socialfeed.command.auth.command.TokenPair;
import com.cqrs.socialfeed.command.auth.usecase.TokenProviderUseCase;
import com.cqrs.socialfeed.domain.auth.*;
import com.cqrs.socialfeed.domain.user.User;
import com.cqrs.socialfeed.domain.user.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider implements TokenProviderUseCase {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private long tokenValidityInMilliseconds;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenInMilliseconds;

    private Key key;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenLogRepository refreshTokenLogRepository;
    private final PasswordEncoder passwordEncoder;

    public JwtTokenProvider(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, RefreshTokenLogRepository refreshTokenLogRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenLogRepository = refreshTokenLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String createToken(String username, long validityInMilliseconds, String tokenType) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("token_type", tokenType)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 사용자 이름 추출
    @Override
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 요청에서 Authorization 헤더 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // DB 기반 UserDetails 생성
    public UserDetails getUserDetails(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));

        return new CustomUserDetails(user);
    }

    @Override
    public String generateToken(User user) {
        return createToken(user.getUsername(), tokenValidityInMilliseconds, "ac");
    }

    @Override
    public String createRefreshToken(String username) {
        return createToken(username, refreshTokenInMilliseconds, "rf");
    }

    @Override
    public void deleteTokenByUser(Long userId) {
        boolean exists = refreshTokenRepository.findByUserId(userId).isPresent();

        refreshTokenRepository.deleteByUserId(userId);

        RefreshTokenLogResultType result = exists ? RefreshTokenLogResultType.LOGOUT_SUCCESS : RefreshTokenLogResultType.ALREADY_LOGGED_OUT;
        refreshTokenLogRepository.save(makeLog(userId, result));
    }

    @Override
    public TokenPair handleRefreshToken(String refreshToken) {
        String username;
        Long userId = null;
        RefreshTokenLogResultType result = RefreshTokenLogResultType.UNKNOWN;

        try {
            username = getUsernameFromToken(refreshToken);
        } catch (Exception e) {
            refreshTokenLogRepository.save(makeLog(null, RefreshTokenLogResultType.INVALID_TOKEN));
            throw new RuntimeException("토큰 파싱 실패");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    refreshTokenLogRepository.save(makeLog(null, RefreshTokenLogResultType.INVALID_USER));
                    return new RuntimeException("사용자 없음");
                });

        userId = user.getId();

        RefreshToken stored = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    refreshTokenLogRepository.save(makeLog(user.getId(), RefreshTokenLogResultType.NO_TOKEN));
                    return new RuntimeException("저장된 RefreshToken 없음");
                });

        if (!stored.getToken().equals(refreshToken)) {
            refreshTokenLogRepository.save(makeLog(userId, RefreshTokenLogResultType.MISMATCH));
            throw new RuntimeException("Refresh Token 불일치");
        }

        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenLogRepository.save(makeLog(userId, RefreshTokenLogResultType.EXPIRED));
            throw new RuntimeException("Refresh Token 만료");
        }

        // 정상 처리
        String newAccessToken = generateToken(user);
        String newRefreshToken = createRefreshToken(user.getUsername());

        refreshTokenRepository.save(new RefreshToken(
                userId,
                newRefreshToken,
                LocalDateTime.now().plusMinutes(refreshTokenInMilliseconds/ (1000 * 60))
        ));

        refreshTokenLogRepository.save(makeLog(userId, RefreshTokenLogResultType.SUCCESS));

        return new TokenPair(newAccessToken, newRefreshToken);
    }

    @Override
    public TokenPair handleToken(LoginTokenCommand command) {

        User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(command.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = generateToken(user);
        String refreshToken = createRefreshToken(user.getUsername());

        refreshTokenRepository.save(new RefreshToken(
                user.getId(),
                refreshToken,
                LocalDateTime.now().plusMinutes(refreshTokenInMilliseconds/ (1000 * 60))
        ));

        return new TokenPair(accessToken, refreshToken);
    }

    private RefreshTokenLog makeLog(Long userId, RefreshTokenLogResultType result) {
        return new RefreshTokenLog(
                userId,
                LocalDateTime.now(),
                RequestContextHolder.currentRequestAttributes() instanceof ServletRequestAttributes attr
                        ? attr.getRequest().getRemoteAddr()
                        : "UNKNOWN",
                RequestContextHolder.currentRequestAttributes() instanceof ServletRequestAttributes attr
                        ? attr.getRequest().getHeader("User-Agent")
                        : "UNKNOWN",
                result
        );
    }

}
