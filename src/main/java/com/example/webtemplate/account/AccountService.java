package com.example.webtemplate.account;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.example.webtemplate.account.AccountDataTypes.AccountType;
import com.example.webtemplate.account.AccountDataTypes.RegisterType;
import com.example.webtemplate.account.AccountDataTypes.RoleType;
import com.example.webtemplate.common.Util;

@Service
public class AccountService {

  private final AccountRepository repository;

  AccountService(AccountRepository repository) {
    this.repository = repository;
  }

  Account register(RegisterType registerType, String email, String password) throws IllegalArgumentException {
    return switch (registerType) {

      case PASSWORD -> {
        var newAccount = buildLocalUserAccount(email, password);
        if (!isValidLocalUserAccount(newAccount)) {
          throw new IllegalArgumentException("Invalid request: " + newAccount);
        }
        yield repository.save(newAccount);
      }

      case ADMIN -> throw new IllegalArgumentException("Admin registration not permitted");
      default -> throw new IllegalArgumentException("Unsupported registration type: " + registerType);
    };
  }

  Account patchAccountPassword(Long id, String newPassword) throws NoSuchElementException {

    if (!Util.isValidPassword(newPassword)) {
      throw new IllegalArgumentException("Invalid password format");
    }

    return repository.findById(id).map(account -> {
      account.setPassword(Util.argon2Hash(newPassword));
      return repository.save(account);
    }).orElseThrow();
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
}
