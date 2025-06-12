package com.inssider.api.domains.account;

import com.inssider.api.common.Util;
import jakarta.annotation.PostConstruct;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("load")
class AccountInitializer {

  @Autowired private AccountRepository accountRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  private Supplier<Account> accountSupplier =
      () -> {
        Account account = Util.accountGenerator().get();
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
      };

  @PostConstruct
  public void init() {
    var account = accountSupplier.get();
    accountRepository.softDelete(account.getId());
  }
}
