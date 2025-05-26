package com.example.webtemplate.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InitialEntityStateTest {

  @Autowired
  private AccountService accountService;

  @Test
  void shouldHaveInitialAccountData() {
    var size = accountService.count();
    assertEquals(3, size);
  }
}
