package com.inssider.api.domains.auth.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.inssider.api.common.Util;
import com.inssider.api.domains.auth.code.email.EmailAuthCode;
import com.inssider.api.domains.auth.code.email.EmailAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class EmailCodeEntityBehaviorTests {

  @Autowired private EmailAuthService service;

  @Test
  @Transactional
  void 이메일_인증코드_생명주기() {

    String email = Util.emailGenerator().get();

    service.challengeEmail(email);
    assertEquals(1, service.countEmailCodes());
    EmailAuthCode entity = service.findById(email).orElseThrow();
    assertEquals(6, entity.getCode().length());
    assertEquals(entity.getCreatedAt().plusSeconds(300), entity.getExpiredAt());

    service.challengeEmail(email);
    assertEquals(1, service.countEmailCodes());

    var newCode = service.findById(email).orElseThrow().getCode();
    assertNotEquals(entity.getCode(), newCode);

    service.verifyEmail(email, newCode);
    assertEquals(0, service.countEmailCodes());
  }
}
