package com.inssider.api.domains;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.inssider.api.common.Util;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountService;
import com.inssider.api.domains.profile.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class EntityBehaviorTest {

  @Autowired private AccountService accountService;
  @Autowired private UserProfileService userProfileService;

  @Test
  @Transactional
  void 계정을_등록할때_사용자프로필이_자동으로_생성되어야한다() {
    // Given - 테스트 환경 준비 (깨끗한 상태)

    // When - 새로운 계정 등록
    register(Util.accountGenerator().get());

    // Then - 사용자 프로필이 정확히 1개 생성되어야 함
    long profileCount = userProfileService.count();
    assertEquals(1, profileCount);
  }

  private Account register(Account account) {
    return accountService.register(
        RegisterType.PASSWORD, account.getEmail(), account.getPassword());
  }
}
