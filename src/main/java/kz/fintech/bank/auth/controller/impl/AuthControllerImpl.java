package kz.fintech.bank.auth.controller.impl;

import kz.fintech.bank.auth.controller.AuthController;
import kz.fintech.bank.auth.dto.request.AuthDto.RegisterRequest;
import kz.fintech.bank.auth.dto.request.AuthDto.UserResponse;
import kz.fintech.bank.auth.dto.request.AuthDto.LoginRequest;
import kz.fintech.bank.auth.dto.response.AuthResponse;
import kz.fintech.bank.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public AuthResponse login(LoginRequest request) {
        return authService.login(request);
    }

    @Override
    public UserResponse me(Jwt jwt) {
        return authService.me(UUID.fromString(jwt.getSubject()));
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        return authService.register(request);
    }


}
