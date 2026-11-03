package kz.fintech.bank.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kz.fintech.bank.auth.dto.request.AuthDto.UserResponse;
import kz.fintech.bank.auth.dto.request.AuthDto.LoginRequest;
import kz.fintech.bank.auth.dto.response.AuthResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public interface AuthController {

    @Operation(
            summary = "Авторизация пользователя",
            description = ""
    )
    AuthResponse login(@Valid @RequestBody LoginRequest request);

    UserResponse me(Authentication authentication, @RequestHeader("Authorization") String authHeader);


}
