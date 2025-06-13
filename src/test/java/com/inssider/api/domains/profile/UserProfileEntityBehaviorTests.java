package com.inssider.api.domains.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.inssider.api.common.Util;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserProfileEntityBehaviorTests {

  @Autowired private AccountService accountService;
  @Autowired private UserProfileService service;
  @Autowired private UserProfileRepository repository;

  @Test
  @Transactional
  void 사용자프로필_저장_및_조회() {
    Account account = register(Util.accountGenerator().get());
    UserProfile userProfile = account.getProfile();
    String profileNickname = service.findUserProfileById(account.getId()).nickname();

    assertNotNull(userProfile);
    assertEquals(account, userProfile.getAccount());
    assertEquals(profileNickname, userProfile.getNickname());
  }

  @Test
  @Transactional
  void 계정_소프트_삭제시_프로필도_소프트_삭제되어야_한다() {
    Account account = register(Util.accountGenerator().get());
    Long accountId = account.getId();
    service.findUserProfileById(accountId); // 프로필 조회

    accountService.softDelete(accountId); // 계정 삭제

    assertEquals(0, service.count()); // 공식적인 삭제 확인
    assertEquals(1, repository.findAllDeleted().size()); // soft-delete 확인
  }

  private Account register(Account account) {
    return accountService.register(
        RegisterType.PASSWORD, account.getEmail(), account.getPassword());
  }
}
