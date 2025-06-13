package com.inssider.api.common;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountAuthenticator;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UtilTest {
  private static final String ENCODED_PASSWORD_PATTERN = "^\\{[a-zA-Z0-9@_]+}\\$.*";

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private AccountService accountService;

  @Autowired private AccountAuthenticator authenticator;

  @Test
  void password_equals() {
    String expected;
    String actual;
    {
      Account account = Util.accountGenerator().get();
      expected = account.getPassword();
      var entity = register(account);
      actual = entity.getPassword();
    }
    assertTrue(passwordEncoder.matches(expected, actual));
  }

  @Test
  void verify_password() {
    String email;
    String plainPassword;
    {
      Account account = Util.accountGenerator().get();
      email = account.getEmail();
      plainPassword = account.getPassword();
      register(account);
    }
    assertNotNull(authenticator.authenticate(email, plainPassword));
  }

  @Test
  void generate_password() {
    String plainPassword = Util.passwordGenerator().get();
    assertNotNull(plainPassword);
    assertTrue(plainPassword.length() >= 8);
    assertFalse(plainPassword.matches(ENCODED_PASSWORD_PATTERN));

    {
      String plainPassword2 = Util.accountGenerator().get().getPassword();
      assertNotNull(plainPassword2);
      assertTrue(plainPassword2.length() >= 8);
      assertFalse(plainPassword2.matches(ENCODED_PASSWORD_PATTERN));
    }

    String hashedPassword = passwordEncoder.encode(plainPassword);
    assertTrue(hashedPassword.matches(ENCODED_PASSWORD_PATTERN));

    String rehashedPassword = passwordEncoder.encode(plainPassword);
    assertTrue(passwordEncoder.matches(plainPassword, rehashedPassword));
  }

  private Account register(Account account) {
    return accountService.register(
        RegisterType.PASSWORD, account.getEmail(), account.getPassword());
  }
}
