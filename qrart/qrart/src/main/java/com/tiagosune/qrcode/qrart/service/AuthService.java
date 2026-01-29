package com.tiagosune.qrcode.qrart.service;

import com.tiagosune.qrcode.qrart.dto.LoginRequest;
import com.tiagosune.qrcode.qrart.dto.AuthResponse;
import com.tiagosune.qrcode.qrart.dto.RegisterRequest;
import com.tiagosune.qrcode.qrart.repository.UsersRepository;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UsersRepository usersRepository;


    /**
     * Registers user; persists if email is unique
     */
    public AuthResponse register(RegisterRequest request) {
        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Usuário já existe!");
        }
        Users user = new Users();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("ROLE_USER");
        usersRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setToken(jwtService.generateToken(user.getEmail()));

        return response;
    }

    public AuthResponse login(LoginRequest request) {

        Users user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Credenciais inválidas!");
        }

        AuthResponse response = new AuthResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setToken(jwtService.generateToken(user.getEmail()));

        return response;
    }

}
