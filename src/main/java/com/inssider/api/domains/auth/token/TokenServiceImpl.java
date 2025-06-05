package com.inssider.api.domains.auth.token;

import com.inssider.api.domains.auth.AuthRequestsDto.AuthorizationCodeLoginRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.LoginRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.PasswordLoginRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.RefreshTokenLoginRequest;
import com.inssider.api.domains.auth.AuthResponsesDto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** 토큰 관리 서비스 구현체 JWT 토큰 생성, 검증, 무효화 등의 기능을 구현 */
@Service
@RequiredArgsConstructor
class TokenServiceImpl implements TokenService {

  private final JwtService jwtService;

  @Override
  public TokenResponse createToken(LoginRequest request) {
    return switch (request) {
      case AuthorizationCodeLoginRequest req -> {
        var authCode = req.uuid();
        yield jwtService.permitAccessTokenByAuthorizationCode(authCode);
      }

      case PasswordLoginRequest req -> {
        // [ ] add validation if needed
        var email = req.email(); // email uniqueness & regex validation
        var password = req.password(); // password regex validation
        yield jwtService.permitTokensByPassword(email, password);
      }

      case RefreshTokenLoginRequest req -> {
        // Refresh token logic - 구현 필요
        throw new UnsupportedOperationException("Refresh token login not implemented yet");
      }
    };
  }

  @Override
  public void revokeToken(String token) {
    var id = jwtService.getAccountIdFromAccessToken(token);
    jwtService.revokeRefreshToken(id);
  }
}
