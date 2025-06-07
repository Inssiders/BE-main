package com.inssider.api.domains.account;

import com.inssider.api.common.Util;
import jakarta.annotation.PostConstruct;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("load")
public class AccountInitializer {
  // reference to repository and add some entities to the database

  private AccountRepository accountRepository;

  private Supplier<Account> accountSupplier =
      () -> accountRepository.save(Util.accountGenerator().get());

  @PostConstruct
  public void init() {
    var account = accountSupplier.get();
    accountRepository.softDelete(account.getId());
  }
}
