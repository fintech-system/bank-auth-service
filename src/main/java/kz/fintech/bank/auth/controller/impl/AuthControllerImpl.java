package kz.fintech.bank.auth.controller.impl;

import kz.fintech.bank.auth.controller.AuthController;
import kz.fintech.bank.auth.dto.request.AuthDto.UserResponse;
import kz.fintech.bank.auth.dto.request.AuthDto.LoginRequest;
import kz.fintech.bank.auth.dto.response.AuthResponse;
import kz.fintech.bank.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public AuthResponse login(LoginRequest request) {
        return authService.login(request);
    }

    @Override
    public UserResponse me(Authentication authentication, String authHeader) {
        String token = authHeader.substring("Bearer ".length());
        return authService.me(authentication.getName(), token);
    }
}
