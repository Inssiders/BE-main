package com.inssider.api.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UtilTest {

  @Autowired private AccountService accountService;

  @Test
  void password_equals() {
    String expected;
    String actual;
    {
      Account account = Util.accountGenerator().get();
      expected = Util.argon2Hash(account.getPassword());
      var entity = accountService.register(account);
      actual = entity.getPassword();
    }
    assertEquals(expected, actual);
  }

  @Test
  void verify_password() {
    String email;
    String password;
    {
      Account account = Util.accountGenerator().get();
      email = account.getEmail();
      password = account.getPassword();
      accountService.register(account);
    }
    Long id = accountService.verifyPassword(email, password);
    assertNotNull(id);
  }
}
