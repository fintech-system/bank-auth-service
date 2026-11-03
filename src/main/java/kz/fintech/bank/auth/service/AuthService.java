package kz.fintech.bank.auth.service;

import kz.fintech.bank.auth.dto.request.AuthDto;
import kz.fintech.bank.auth.dto.request.AuthDto.LoginRequest;
import kz.fintech.bank.auth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthDto.UserResponse me(String email, String token);
}
