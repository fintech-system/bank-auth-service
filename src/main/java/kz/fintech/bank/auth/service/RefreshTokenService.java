package kz.fintech.bank.auth.service;

import kz.fintech.bank.auth.dto.request.TokenDto.IssuedRefreshToken;
import kz.fintech.bank.auth.entity.AuthUser;

public interface RefreshTokenService {

    IssuedRefreshToken issue(AuthUser user);

}
