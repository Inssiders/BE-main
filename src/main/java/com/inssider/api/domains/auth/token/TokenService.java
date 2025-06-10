package com.inssider.api.domains.auth.token;

import com.inssider.api.domains.auth.AuthRequestsDto.LoginRequest;
import com.inssider.api.domains.auth.AuthResponsesDto.TokenResponse;

/** 토큰 관리 서비스 인터페이스 JWT 토큰 생성, 검증, 무효화 등의 기능을 담당 */
public interface TokenService {

  /**
   * 로그인 요청에 따라 토큰을 생성합니다.
   *
   * @param request 로그인 요청 (AuthorizationCode, Password, RefreshToken)
   * @return 토큰 응답
   */
  TokenResponse createToken(LoginRequest request);

  /**
   * 토큰을 무효화합니다.
   *
   * @param token 무효화할 토큰
   */
  void revokeToken(String token);
}
