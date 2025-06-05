package com.inssider.api.domains.account;

import com.inssider.api.common.Util;
import com.inssider.api.domains.account.AccountDataTypes.AccountType;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountDataTypes.RoleType;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AccountServiceImpl implements AccountService {

  private final AccountRepository repository;
  private final JwtDecoder jwtDecoder;

  @Override
  public Account register(RegisterType registerType, String email, String password)
      throws IllegalArgumentException {
    return switch (registerType) {
      case PASSWORD -> {
        var newAccount = buildLocalUserAccount(email, password);
        if (!isValidLocalUserAccount(newAccount)) {
          throw new IllegalArgumentException("Invalid request: " + newAccount);
        }
        handleExistingSoftDeletedAccount(email);
        yield register(newAccount);
      }

      case ADMIN -> throw new IllegalArgumentException("Admin registration not permitted");
      default ->
          throw new IllegalArgumentException("Unsupported registration type: " + registerType);
    };
  }

  /**
   * 기존에 soft delete된 계정이 있다면 완전 삭제 처리
   *
   * @param email 확인할 이메일
   */
  private void handleExistingSoftDeletedAccount(String email) {
    repository
        .findByEmail(email)
        .ifPresent(
            existingAccount -> {
              if (existingAccount.isDeleted()) {
                repository.delete(existingAccount);
              } else {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + email);
              }
            });
  }

  @Override
  public Account register(Account account) throws IllegalArgumentException {
    return repository.save(account);
  }

  private boolean isValidLocalUserAccount(Account account) {
    return account.getRole() == RoleType.USER
        && account.getAccountType().isLocal()
        && Util.isValidEmail(account.getEmail())
        && Util.isValidPassword(account.getPassword());
  }

  private Account buildLocalUserAccount(String email, String password) {
    return Account.builder()
        .accountType(AccountType.PASSWORD)
        .email(email)
        .password(Util.argon2Hash(password))
        .role(RoleType.USER)
        .build();
  }

  @Override
  public Account patchAccountPassword(Long id, String newPassword) throws NoSuchElementException {

    if (!Util.isValidPassword(newPassword)) {
      throw new IllegalArgumentException("Invalid password format");
    }

    return repository
        .findById(id)
        .map(
            account -> {
              account.setPassword(Util.argon2Hash(newPassword));
              return repository.save(account);
            })
        .orElseThrow();
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
    return repository.findById(id).orElseThrow();
  }

  @Override
  public Long verifyPassword(String email, String rawPassword)
      throws IllegalArgumentException, NoSuchElementException {
    var entity = repository.findByEmail(email).orElseThrow();
    if (!Util.argon2Hash(rawPassword).equals(entity.getPassword())) {
      throw new IllegalArgumentException(
          rawPassword + " is not matched with " + entity.getPassword());
    }
    return entity.getId();
  }
}
