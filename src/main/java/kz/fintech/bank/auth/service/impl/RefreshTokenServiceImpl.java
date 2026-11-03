package kz.fintech.bank.auth.service.impl;

import kz.fintech.bank.auth.dto.request.TokenDto.IssuedRefreshToken;
import kz.fintech.bank.auth.entity.AuthUser;
import kz.fintech.bank.auth.entity.RefreshToken;
import kz.fintech.bank.auth.jwt.JwtProperties;
import kz.fintech.bank.auth.repo.RefreshTokenRepository;
import kz.fintech.bank.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties properties;
    private final Clock clock = Clock.systemUTC();

    @Override
    public IssuedRefreshToken issue(AuthUser user) {
        String token = generateOpaqueToken();
        Instant expiresAt = clock.instant().plus(properties.refreshTokenTtl());
        refreshTokenRepository.save(new RefreshToken(user, hash(token), expiresAt));
        return new IssuedRefreshToken(token, expiresAt);
    }

    private String generateOpaqueToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    static String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
