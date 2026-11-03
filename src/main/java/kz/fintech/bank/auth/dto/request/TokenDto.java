package kz.fintech.bank.auth.dto.request;

import java.time.Instant;

public final class TokenDto {

    private TokenDto() {
    }

    public record IssuedRefreshToken(
            String value,
            Instant expiresAt
    ) {
    }
}
