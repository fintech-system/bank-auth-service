package kz.fintech.bank.auth.exception;

import kz.fintech.bank.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    EMAIL_ALREADY_USED(HttpStatus.CONFLICT),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED);

    private final HttpStatus status;

    @Override
    public String code() {
        return name();
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}
