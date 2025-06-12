package com.inssider.api.common.model;

import com.inssider.api.domains.account.Account;
import lombok.Getter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AccountJwtAuthenticationToken extends JwtAuthenticationToken {
  @Getter private Account account;

  public AccountJwtAuthenticationToken(
      JwtAuthenticationToken jwtAuthenticationToken, Account account) {
    super(jwtAuthenticationToken.getToken(), jwtAuthenticationToken.getAuthorities());
    this.account = account;
  }

  @Override
  public Account getPrincipal() {
    return account;
  }
}
