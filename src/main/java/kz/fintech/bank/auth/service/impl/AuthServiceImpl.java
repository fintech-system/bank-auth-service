package kz.fintech.bank.auth.service.impl;

import kz.fintech.bank.auth.dto.request.AuthDto.LoginRequest;
import kz.fintech.bank.auth.dto.request.AuthDto.RegisterRequest;
import kz.fintech.bank.auth.dto.request.AuthDto.UserResponse;
import kz.fintech.bank.auth.dto.request.TokenDto.IssuedRefreshToken;
import kz.fintech.bank.auth.dto.response.AuthResponse;
import kz.fintech.bank.auth.entity.AuthUser;
import kz.fintech.bank.auth.entity.enums.AccountStatus;
import kz.fintech.bank.auth.entity.enums.Role;
import kz.fintech.bank.auth.exception.AuthErrorCode;
import kz.fintech.bank.auth.jwt.JwtService;
import kz.fintech.bank.auth.repo.AuthUserRepository;
import kz.fintech.bank.auth.service.AuthService;
import kz.fintech.bank.auth.service.RefreshTokenService;
import kz.fintech.bank.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {

        String email = normalizeEmail(request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password())
        );

        AuthUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(AuthErrorCode.USER_NOT_FOUND));

        return createAuthResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse me(UUID userId) {
        AuthUser user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(AuthErrorCode.USER_NOT_FOUND));
        return createUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(AuthErrorCode.EMAIL_ALREADY_USED, email);
        }

        AuthUser user = new AuthUser(
                request.firstName(),
                request.lastName(),
                email,
                encoder.encode(request.password()),
                Role.CUSTOMER,
                AccountStatus.ACTIVE
        );
        return createUserResponse(userRepository.save(user));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
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

    private UserResponse createUserResponse(AuthUser user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name()
        );
    }
}
