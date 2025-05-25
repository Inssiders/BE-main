package com.example.webtemplate.account;

import com.example.webtemplate.common.Util;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository repository;

  AccountService(AccountRepository repository) {
    this.repository = repository;
  }

  List<Account> getAllAccounts() {
    return repository.findAll();
  }

  Account createAccount(Account newAccount) {
    return repository.save(newAccount);
  }

  Account patchAccountPassword(Long id, String newPassword)
      throws NoSuchElementException {
    return repository.findById(id).map(account -> {
      if (newPassword != null && !newPassword.isBlank()) {
        account.setPassword(Util.argon2Hash(newPassword));
      }
      return repository.save(account);
    }).orElseThrow();
  }

  Account findById(Long id) throws NoSuchElementException {
    return repository.findById(id).orElseThrow();
  }

  void save(Account account) {
    repository.save(account);
  }

  void deleteById(Long id) {
    repository.deleteById(id);
  }
}
