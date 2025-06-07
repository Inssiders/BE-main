package com.inssider.api.domains.auth;

import com.inssider.api.domains.auth.AuthRequestsDto.LoginRequest;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailCodeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailVerificationResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.TokenResponse;
import com.inssider.api.domains.auth.code.email.EmailAuthCode;
import com.inssider.api.domains.auth.code.email.EmailAuthService;
import com.inssider.api.domains.auth.token.TokenService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/** 통합 인증 서비스 구현체 이메일 인증과 토큰 관리 서비스를 조합하여 통합 인증 기능을 제공 */
@Service
@RequiredArgsConstructor
@Primary
class AuthServiceImpl implements AuthService {

  private final EmailAuthService emailAuthService;
  private final TokenService tokenService;

  // === EmailAuthService 메서드 위임 ===

  @Override
  public Long countEmailCodes() {
    return emailAuthService.countEmailCodes();
  }

  @Override
  public EmailCodeResponse challengeEmail(String email) {
    return emailAuthService.challengeEmail(email);
  }

  @Override
  public EmailVerificationResponse verifyEmail(String email, String code) {
    return emailAuthService.verifyEmail(email, code);
  }

  @Override
  public Optional<EmailAuthCode> findById(String email) {
    return emailAuthService.findById(email);
  }

  // === TokenService 메서드 위임 ===

  @Override
  public TokenResponse createToken(LoginRequest request) {
    return tokenService.createToken(request);
  }

  @Override
  public void revokeToken(String token) {
    tokenService.revokeToken(token);
  }
}
