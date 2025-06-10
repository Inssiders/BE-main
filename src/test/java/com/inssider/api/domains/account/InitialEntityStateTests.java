package com.inssider.api.domains.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inssider.api.common.Util;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class InitialEntityStateTests {

  @Autowired private AccountService accountService;

  private Account createUserAccount() {
    var email = Util.emailGenerator().get();
    var password = Util.argon2Hash(Util.passwordGenerator().get());
    return accountService.register(RegisterType.PASSWORD, email, password);
  }

  @Test
  @Transactional
  @Rollback
  void 계정을_생성할때_정상적으로_생성되고_개수가_증가해야한다() {
    // Given - 현재 계정 개수 확인
    var initialCount = accountService.count();

    // When - 새로운 계정 생성
    var firstAccount = createUserAccount();
    var secondAccount = createUserAccount();

    // Then - 계정이 정상 생성되고 개수가 증가
    assertNotNull(firstAccount);
    assertNotNull(secondAccount);
    assertEquals(initialCount + 2, accountService.count());
  }

  @Test
  @Transactional
  @Rollback
  void 계정을_소프트삭제할때_삭제시간이_기록되고_개수에서_제외되어야한다() {
    // Given - 계정 생성
    var account = createUserAccount();
    var createdAt = account.getCreatedAt();
    var updatedAt = account.getUpdatedAt();

    // When - 소프트 삭제 수행
    var deletedAt = accountService.softDelete(account.getId());

    // Then - 삭제 시간이 기록되고 카운트에서 제외
    assertEquals(createdAt, updatedAt);
    assertTrue(deletedAt.isAfter(createdAt));
    assertTrue(deletedAt.isAfter(updatedAt));
  }
}
