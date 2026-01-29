package com.tiagosune.qrcode.qrart.controller;

import com.tiagosune.qrcode.qrart.dto.AuthResponse;
import com.tiagosune.qrcode.qrart.dto.LoginRequest;
import com.tiagosune.qrcode.qrart.dto.RegisterRequest;
import com.tiagosune.qrcode.qrart.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
