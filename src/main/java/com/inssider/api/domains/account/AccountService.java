package com.inssider.api.domains.account;

import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface AccountService {
  Account register(RegisterType registerType, String email, String password)
      throws IllegalArgumentException;

  Account register(Account account) throws IllegalArgumentException;

  Account patchAccountPassword(Long id, String newPassword) throws NoSuchElementException;

  long count();

  LocalDateTime softDelete(Long id) throws NoSuchElementException;

  // Repository 메서드들을 위한 서비스 메서드들
  boolean existsByEmail(String email);

  Optional<Account> findByEmail(String email);
}
