package com.example.webtemplate.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InitialEntityStateTest {

  @Autowired
  private AccountRepository repository;

  @Test
  void shouldHaveInitialAccountData() {
    var accounts = repository.findAll();
    assertEquals(3, accounts.size());
  }
}
