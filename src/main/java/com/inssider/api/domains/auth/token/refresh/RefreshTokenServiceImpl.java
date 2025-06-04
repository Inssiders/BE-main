package com.inssider.api.domains.auth.token.refresh;

import org.springframework.stereotype.Service;

/** RefreshToken 서비스 구현체 기본 CRUD는 BaseServiceImpl에서 상속 */
@Service
class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepository repository;

  RefreshTokenServiceImpl(RefreshTokenRepository repository) {
    this.repository = repository;
  }

  @Override
  public RefreshTokenRepository getRepository() {
    return repository;
  }
}
