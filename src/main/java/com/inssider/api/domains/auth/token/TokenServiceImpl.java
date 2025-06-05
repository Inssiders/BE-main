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
      case AuthorizationCodeLoginRequest authCodeRequest -> {
        var authCode = authCodeRequest.uuid();
        yield jwtService.permitAccessTokenByAuthorizationCode(authCode);
      }
      case PasswordLoginRequest passwordLoginRequest -> {
        var email = passwordLoginRequest.email();
        var password = passwordLoginRequest.password();
        yield jwtService.permitTokensByPassword(email, password);
      }
      case RefreshTokenLoginRequest refreshTokenLoginRequest -> {
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
