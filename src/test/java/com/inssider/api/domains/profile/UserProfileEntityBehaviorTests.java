package com.inssider.api.domains.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.inssider.api.common.Util;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserProfileEntityBehaviorTests {
  @Autowired private AccountService accountService;

  @Autowired private UserProfileService userProfileService;

  @Test
  @Transactional
  void 사용자프로필_저장_및_조회() {
    Account account = accountService.register(Util.accountGenerator().get());
    UserProfile userProfile = account.getProfile();
    String profileNickname = userProfileService.findUserProfileById(account.getId()).nickname();

    assertNotNull(userProfile);
    assertEquals(account, userProfile.getAccount());
    assertEquals(profileNickname, userProfile.getNickname());
  }
}
