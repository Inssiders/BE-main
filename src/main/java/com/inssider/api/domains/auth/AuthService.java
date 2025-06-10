package com.inssider.api.domains.auth;

import com.inssider.api.domains.auth.code.email.EmailAuthService;
import com.inssider.api.domains.auth.token.TokenService;

/** 통합 인증 서비스 인터페이스 이메일 인증과 토큰 관리 기능을 통합하여 제공 */
public interface AuthService extends EmailAuthService, TokenService {
  // 하위 서비스들의 메서드를 모두 상속받아 통합 인터페이스 역할
}
