package kz.fintech.bank.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kz.fintech.bank.auth.dto.request.AuthDto.RegisterRequest;
import kz.fintech.bank.auth.dto.request.AuthDto.UserResponse;
import kz.fintech.bank.auth.dto.request.AuthDto.LoginRequest;
import kz.fintech.bank.auth.dto.response.AuthResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public interface AuthController {

    @Operation(
            summary = "Авторизация пользователя",
            description = ""
    )
    @PostMapping("/login")
    AuthResponse login(@Valid @RequestBody LoginRequest request);

    @Operation(
            summary = "Данные пользователя",
            description = ""
    )
    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    UserResponse me(@AuthenticationPrincipal Jwt jwt);

    @Operation(
            summary = "Регистрация пользователя",
            description = ""
    )
    @PostMapping("/register")
    UserResponse register(@Valid @RequestBody RegisterRequest request);

}
