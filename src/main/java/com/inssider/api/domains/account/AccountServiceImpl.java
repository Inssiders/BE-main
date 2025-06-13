package com.inssider.api.domains.account;

import com.inssider.api.common.Util;
import com.inssider.api.domains.account.AccountDataTypes.AccountType;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountDataTypes.RoleType;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AccountServiceImpl implements AccountService {
  private final PasswordEncoder passwordEncoder;
  private final AccountRepository repository;
  private final JwtDecoder jwtDecoder;

  @Override
  public Account register(RegisterType registerType, String email, String password) {

    password = passwordEncoder.encode(password);

    return switch (registerType) {
      case PASSWORD -> {
        var newAccount =
            Account.builder()
                .accountType(AccountType.PASSWORD)
                .role(RoleType.USER)
                .email(email)
                .password(password)
                .build();

        repository
            .findByEmail(email)
            .ifPresent(
                existingAccount -> {
                  if (existingAccount.isDeleted()) {
                    repository.delete(existingAccount);
                    repository.flush();
                  }
                });

        yield repository.save(newAccount);
      }

      case ADMIN -> throw new IllegalArgumentException("Admin registration not permitted");
      default ->
          throw new IllegalArgumentException("Unsupported registration type: " + registerType);
    };
  }

  @Override
  public Account patchAccountPassword(Long id, String newPassword) throws NoSuchElementException {
    // PATCH /api/accounts/me/password
    if (!Util.isValidPassword(newPassword)) {
      throw new IllegalArgumentException("Invalid password format");
    }
    var entity = repository.findById(id).orElseThrow();
    entity.setPassword(passwordEncoder.encode(newPassword));
    return repository.save(entity);
  }

  @Override
  public LocalDateTime softDelete(Long id) throws NoSuchElementException {
    var account = repository.findById(id).orElseThrow();
    account.softDelete();
    return repository.save(account).getDeletedAt();
  }

  @Override
  public AccountRepository getRepository() {
    return repository;
  }

  @Override
  public Account getAccountFromToken(String authorizationHeader) {
    var token = authorizationHeader.replace("Bearer ", "");
    var claims = jwtDecoder.decode(token);
    Long id = Long.parseLong(claims.getSubject());
    var account = repository.findById(id).orElseThrow();

    if (account.getRefreshToken() == null) {
      throw new IllegalArgumentException("로그인 상태가 아닙니다. 다시 로그인 해주세요.");
    }
    if (account.isDeleted()) {
      throw new IllegalArgumentException("이미 탈퇴한 계정입니다.");
    }

    return account;
  }
}
