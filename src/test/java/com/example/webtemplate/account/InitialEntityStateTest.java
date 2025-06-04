package com.example.webtemplate.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.webtemplate.common.Util;

@SpringBootTest
class InitialEntityStateTest {

  @Autowired
  private AccountService accountService;

  @Test
  void shouldHaveInitialAccountData() {
    var size = accountService.count();
    assertEquals(2, size);
  }

  @Test
  @Transactional
  @Rollback
  void softDelete() {
    var account = accountService.register(
        AccountDataTypes.RegisterType.PASSWORD,
        Util.emailGenerator().get(),
        Util.argon2Hash(Util.passwordGenerator().get()));

    var createdAt = account.getCreatedAt();
    var updatedAt = account.getUpdatedAt();
    var deletedAt = accountService.softDelete(account.getId());

    assertEquals(createdAt, updatedAt);
    assertTrue(deletedAt.isAfter(createdAt));
    assertTrue(deletedAt.isAfter(updatedAt));
  }
}
