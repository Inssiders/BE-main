package com.inssider.api.domains.account;

import com.inssider.api.domains.account.AccountDataTypes.AccountType;
import com.inssider.api.domains.account.AccountDataTypes.RoleType;
import com.inssider.api.domains.auth.code.AuthCodeService;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountAuthenticator {

  private final AccountRepository repository;

  // common
  private final PasswordEncoder passwordEncoder;
  private final JwtDecoder jwtDecoder;

  // auth-code-related
  private final AuthCodeService authCodeService;

  public Account authenticate(String email, String password)
      throws NoSuchElementException, IllegalArgumentException {
    Account account = repository.findByEmail(email).orElseThrow();
    if (account.isDeleted()) {
      throw new NoSuchElementException();
    }
    if (!passwordEncoder.matches(password, account.getPassword())) {
      throw new IllegalArgumentException("Invalid email or password");
    }
    return account;
  }

  public Account redeemAuthorizationCode(UUID authCodeId) {
    String email = authCodeService.redeem(authCodeId).getEmail();
    return repository
        .findByEmail(email)
        .orElse(
            Account.builder()
                .accountType(AccountType.PASSWORD)
                .role(RoleType.USER)
                .email(email)
                .build());
  }

  public Account getAccountFromToken(String token) throws NoSuchElementException {
    Long id = Long.valueOf(jwtDecoder.decode(token).getSubject());
    return repository.findById(id).orElseThrow();
  }
}
