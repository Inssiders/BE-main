package com.inssider.api.domains.account;

import com.inssider.api.common.Util;
import jakarta.annotation.PostConstruct;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
@Configuration
@RequiredArgsConstructor
@Profile("dev")
class AccountInitializer {
  // reference to repository and add some entities to the database

  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  private Supplier<Account> accountSupplier =
      () -> {
        Account account = Util.accountGenerator().get();
        log.info("Password {}", account.getPassword());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
      };

  @PostConstruct
  public void init() {
    var account = accountSupplier.get();
//    accountRepository.softDelete(account.getId());
  }
}
