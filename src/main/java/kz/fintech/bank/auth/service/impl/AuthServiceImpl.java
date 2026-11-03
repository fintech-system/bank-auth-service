package kz.fintech.bank.auth.service.impl;

import kz.fintech.bank.auth.dto.request.AuthDto.LoginRequest;
import kz.fintech.bank.auth.dto.request.AuthDto.UserResponse;
import kz.fintech.bank.auth.dto.request.TokenDto.IssuedRefreshToken;
import kz.fintech.bank.auth.dto.response.AuthResponse;
import kz.fintech.bank.auth.entity.AuthUser;
import kz.fintech.bank.auth.entity.enums.AccountStatus;
import kz.fintech.bank.auth.jwt.JwtService;
import kz.fintech.bank.auth.repo.AuthUserRepository;
import kz.fintech.bank.auth.service.AuthService;
import kz.fintech.bank.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {

        String email = normalizeEmail(request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password())
        );

        AuthUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user was not found"));

        if (user.getStatus().equals(AccountStatus.BLOCKED)) {
            throw new AccessDeniedException("User is blocked");
        }

        return createAuthResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse me(String email, String token) {
        AuthUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user was not found"));

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name()
        );
    }

    private AuthResponse createAuthResponse(AuthUser user) {

        Jwt accessToken = jwtService.issueAccessToken(user);
        IssuedRefreshToken refreshToken = refreshTokenService.issue(user);

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                accessToken.getTokenValue(),
                accessToken.getExpiresAt(),
                refreshToken.value(),
                refreshToken.expiresAt()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
