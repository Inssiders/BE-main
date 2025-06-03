package com.inssider.api.domains.account;

import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public interface AccountService {
  Account register(RegisterType registerType, String email, String password)
      throws IllegalArgumentException;

  Account register(Account account) throws IllegalArgumentException;

  Account patchAccountPassword(Long id, String newPassword) throws NoSuchElementException;

  long count();

  LocalDateTime softDelete(Long id) throws NoSuchElementException;
}
