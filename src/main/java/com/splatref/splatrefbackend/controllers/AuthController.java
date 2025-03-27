package com.splatref.splatrefbackend.controllers;

import com.splatref.splatrefbackend.auth.entities.RefreshToken;
import com.splatref.splatrefbackend.auth.entities.User;
import com.splatref.splatrefbackend.auth.services.AuthService;
import com.splatref.splatrefbackend.auth.services.JwtService;
import com.splatref.splatrefbackend.auth.services.RefreshTokenService;
import com.splatref.splatrefbackend.auth.utils.*;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/*")
@CrossOrigin(origins = "http://localhost:3000/*")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthUserFacingResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, "refreshToken=" + response.getRefreshToken() + " ; HttpOnly; Path=/api/v1/auth")
                .body(new AuthUserFacingResponse(response.getAccessToken(), response.getHandle(), response.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUserFacingResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, "refreshToken=" + response.getRefreshToken() + " ; HttpOnly; Path=/api/v1/auth")
                .body(new AuthUserFacingResponse(response.getAccessToken(), response.getHandle(), response.getEmail()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthUserFacingResponse> refreshToken(@CookieValue(value = "refreshToken") String cookieRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(cookieRefreshToken);
        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthUserFacingResponse.builder()
                .accessToken(accessToken)
                .handle(user.getHandle())
                .email(user.getEmail())
                .build());
    }
}
