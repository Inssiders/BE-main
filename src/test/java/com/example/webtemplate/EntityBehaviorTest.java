package com.example.webtemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.webtemplate.account.AccountDataTypes;
import com.example.webtemplate.account.AccountService;
import com.example.webtemplate.common.Util;
import com.example.webtemplate.profile.UserProfileService;

@SpringBootTest
class EntityBehaviorTest {

  @Autowired
  private AccountService accountService;

  @Autowired
  private UserProfileService userProfileService;

  @Test
  void shouldCreateUserProfileWhenAccountCreated() {

    long initialSize = userProfileService.count();

    accountService.register(
        AccountDataTypes.RegisterType.PASSWORD,
        Util.emailGenerator().get(),
        Util.argon2Hash(Util.passwordGenerator().get()));

    long newSize = userProfileService.count();

    assertEquals(initialSize + 1, newSize);
  }
}
