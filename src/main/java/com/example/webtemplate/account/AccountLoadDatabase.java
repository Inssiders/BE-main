package com.example.webtemplate.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.webtemplate.account.model.AccountType;
import com.example.webtemplate.account.model.RoleType;

@Configuration
@Profile("dev")
class AccountLoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(
      AccountLoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(AccountRepository repository) {
    return _ -> {
      log.info("Preloading {}", repository.save(
          new Account(AccountType.LOCAL, RoleType.USER,
              "asd1@gmail.com", "asd", "asd")));
      log.info("Preloading {}", repository.save(
          new Account(AccountType.LOCAL, RoleType.USER,
              "asd2@gmail.com", "asd", "asd")));
    };
  }
}