package com.example.webtemplate.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.webtemplate.account.AccountDataTypes.AccountType;
import com.example.webtemplate.account.AccountDataTypes.RoleType;

@Configuration
@Profile("dev")
class AccountLoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(AccountLoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(AccountRepository repository) {
    return _ -> {

      log.info("Preloading {}", repository
          .save(Account.builder()
              .accountType(AccountType.OTHER)
              .role(RoleType.SUPERADMIN)
              .email("asd3@gmail.com")
              .password("password3")
              .build()));

      repository.softDelete(1L);

      log.info("Preloading {}", repository
          .save(Account.builder()
              .accountType(AccountType.PASSWORD)
              .role(RoleType.CONTENT_MANAGER)
              .email("asd2@gmail.com")
              .password("password2")
              .build()));

      log.info("Preloading {}", repository
          .save(Account.builder()
              .accountType(AccountType.PASSWORD)
              .role(RoleType.USER)
              .email("asd1@gmail.com")
              .password("password1")
              .build()));
    };
  }
}
