package kz.fintech.bank.auth.service;

import kz.fintech.bank.auth.dto.request.AuthDto.RegisterRequest;
import kz.fintech.bank.auth.dto.request.AuthDto.UserResponse;
import kz.fintech.bank.auth.dto.request.AuthDto.LoginRequest;
import kz.fintech.bank.auth.dto.response.AuthResponse;

import java.util.UUID;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    UserResponse me(UUID userId);

    UserResponse register(RegisterRequest request);
}
