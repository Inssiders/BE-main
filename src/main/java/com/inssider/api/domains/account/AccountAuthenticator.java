package com.inssider.api.domains.account;

import com.inssider.api.domains.auth.code.AuthCodeService;
import com.inssider.api.domains.auth.code.AuthorizationCode;
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

  public Account redeemAuthorizationCode(UUID authCodeId) throws NoSuchElementException {
    AuthorizationCode code = authCodeService.consume(authCodeId);
    return repository.findByEmail(code.getEmail()).orElseThrow();
  }

  public Account getAccountFromToken(String token) throws NoSuchElementException {
    Long id = Long.valueOf(jwtDecoder.decode(token).getSubject());
    return repository.findById(id).orElseThrow();
  }
}
