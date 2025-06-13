package com.inssider.api.domains.auth;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenWithAuthorizationCodeRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenWithPasswordRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenWithRefreshTokenRequest;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailChallengeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailVerifyResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthTokenResponse;
import com.inssider.api.domains.auth.code.AuthCodeService;
import com.inssider.api.domains.auth.token.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/** 통합 인증 서비스 구현체 이메일 인증과 토큰 관리 서비스를 조합하여 통합 인증 기능을 제공 */
@Service
@RequiredArgsConstructor
@Primary
class AuthServiceImpl implements AuthService {

  private final AuthCodeService codeService;
  private final AuthTokenService tokenService;

  // === EmailAuthService 메서드 위임 ===

  @Override
  public AuthEmailChallengeResponse challengeEmail(String email) {
    return codeService.challengeEmail(email);
  }

  @Override
  public AuthEmailVerifyResponse verifyEmail(String email, String code) {
    return codeService.verifyEmail(email, code);
  }

  // === TokenService 메서드 위임 ===

  @Override
  public AuthTokenResponse createTokens(AuthTokenRequest request) {
    return switch (request) {
      case AuthTokenWithPasswordRequest req -> {
        var email = req.email();
        var rawPassword = req.password();
        yield tokenService.permitTokensByPassword(email, rawPassword);
      }
      case AuthTokenWithRefreshTokenRequest req -> {
        var refreshToken = req.refreshToken();
        yield tokenService.permitTokensByRefreshToken(refreshToken);
      }
      case AuthTokenWithAuthorizationCodeRequest req -> {
        yield tokenService.permitTokensByAuthorizationCode(req.uuid());
      }
    };
  }

  @Override
  public void revokeRefreshToken(Account account) {
    tokenService.revokeRefreshToken(account);
  }
}
