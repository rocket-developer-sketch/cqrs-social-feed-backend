package com.cqrs.socialfeed.api.auth;

import com.cqrs.socialfeed.api.security.CustomUserDetails;
import com.cqrs.socialfeed.command.auth.command.LoginTokenCommand;
import com.cqrs.socialfeed.command.auth.command.TokenPair;
import com.cqrs.socialfeed.command.auth.usecase.TokenProviderUseCase;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final static String AUTH_REFRESH_PATH = "/auth/refresh";
    private final static String AUTH_REFRESH_COOKIE_NAME = "refreshToken";

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenInMilliseconds;
    private Duration refreshTokenCookieMaxAge;

    private final TokenProviderUseCase tokenProviderUseCase;
    private final SignupCommandHandler signupCommandHandler;

    public AuthController(TokenProviderUseCase tokenProviderUseCase, SignupCommandHandler signupCommandHandler) {
        this.tokenProviderUseCase = tokenProviderUseCase;
        this.signupCommandHandler = signupCommandHandler;
    }

    @PostConstruct
    public void init() {
        this.refreshTokenCookieMaxAge = Duration.ofSeconds(refreshTokenInMilliseconds / 1000);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest request) {
        signupCommandHandler.handle(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * refreshToken 쿠키를 사용해 Access Token 재발급 → refreshToken 쿠키 갱신 (rotation)
     * */
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh (
            @CookieValue("refreshToken") String refreshToken, // 쿠키에서 읽음
            HttpServletResponse response
    ) {
        TokenPair tokenPair = tokenProviderUseCase.handleRefreshToken(refreshToken);

        // (선택) RefreshToken도 rotation 시 쿠키 갱신
        ResponseCookie cookie = ResponseCookie.from(AUTH_REFRESH_COOKIE_NAME, tokenPair.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path(AUTH_REFRESH_PATH)
                .sameSite("Strict")
                .maxAge(refreshTokenCookieMaxAge)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new RefreshTokenResponse(tokenPair.getAccessToken(), tokenPair.getRefreshToken()));
    }

    /**
     * username/password로 로그인 → 토큰 발급 + refreshToken 쿠키 설정
     * */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        TokenPair tokenPair = tokenProviderUseCase.handleToken(new LoginTokenCommand(request.username(), request.password()));

        // Refresh Token을 쿠키로 내려주기
//        만약 SameSite("Strict")인데, 클라이언트와 서버가 서로 다른 포트(localhost:3000 ↔ localhost:8080)라면 → 쿠키는 막힙니다.
//        이럴 땐 **SameSite("None") + Secure(true)**로 바꾸세요.
        ResponseCookie refreshCookie = ResponseCookie.from(AUTH_REFRESH_COOKIE_NAME, tokenPair.getRefreshToken())
                .httpOnly(true)
                //.secure(true) // 	HTTPS 통신에서만 쿠키 전송됨. 로컬에서는 false로 테스트 가능
                .secure(false)
                .path(AUTH_REFRESH_PATH)
                .sameSite("Strict")
                .maxAge(refreshTokenCookieMaxAge)
                .build();

        response.addHeader("Set-Cookie", refreshCookie.toString());

        // Access Token은 JSON body로 반환
        return ResponseEntity.ok(new LoginResponse(tokenPair.getAccessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails user, HttpServletResponse response) {
        tokenProviderUseCase.deleteTokenByUser(user.getId());

        ResponseCookie deleteCookie = ResponseCookie.from(AUTH_REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path(AUTH_REFRESH_PATH)
                .sameSite("Strict")
                .maxAge(0) // 삭제
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString());

        return ResponseEntity.noContent().build();
    }
}
