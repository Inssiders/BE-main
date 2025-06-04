package com.inssider.api.domains.auth.code;

import org.springframework.stereotype.Service;

@Service
class AuthorizationCodeServiceImpl implements AuthorizationCodeService {

  private final AuthorizationCodeRepository repository;

  AuthorizationCodeServiceImpl(AuthorizationCodeRepository repository) {
    this.repository = repository;
  }

  @Override
  public AuthorizationCodeRepository getRepository() {
    return repository;
  }
}
