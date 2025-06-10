package com.inssider.api.domains.auth;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthorizationCodeLoginRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.LoginRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.PasswordLoginRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.TokenRefreshLoginRequest;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailCodeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailVerificationResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.TokenResponse;
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
  public EmailCodeResponse challengeEmail(String email) {
    return codeService.challengeEmail(email);
  }

  @Override
  public EmailVerificationResponse verifyEmail(String email, String code) {
    return codeService.verifyEmail(email, code);
  }

  // === TokenService 메서드 위임 ===

  @Override
  public TokenResponse createTokens(LoginRequest request) {
    return switch (request) {
      case PasswordLoginRequest req -> {
        var email = req.email();
        var rawPassword = req.password();
        yield tokenService.permitTokensByPassword(email, rawPassword);
      }
      case TokenRefreshLoginRequest req -> {
        var refreshToken = req.refreshToken();
        yield tokenService.permitTokensByRefreshToken(refreshToken);
      }
      case AuthorizationCodeLoginRequest req -> {
        yield tokenService.permitTokensByAuthorizationCode(req.uuid());
      }
    };
  }

  @Override
  public void revokeRefreshToken(Account account) {
    tokenService.revokeRefreshToken(account);
  }
}
