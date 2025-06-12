package com.inssider.api.domains.auth.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.inssider.api.common.Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class EmailCodeEntityBehaviorTests {

  @Autowired private AuthCodeService service;

  @Autowired private EmailAuthenticationCodeTestRepository emailAuthenticationCodeRepository;

  @Test
  @Transactional
  void 이메일_인증코드_생명주기() {

    // 0. random email
    String email = Util.emailGenerator().get();

    // 1. first challenge
    service.challengeEmail(email);
    assertEquals(1, emailAuthenticationCodeRepository.count());
    EmailAuthenticationCode entity =
        emailAuthenticationCodeRepository.findById(email).orElseThrow();
    assertEquals(6, entity.getCode().length());
    assertEquals(entity.getCreatedAt().plusSeconds(300), entity.getExpiredAt());

    // 2. second challenge
    service.challengeEmail(email);
    assertEquals(1, emailAuthenticationCodeRepository.count());

    var newCode = emailAuthenticationCodeRepository.findById(email).orElseThrow().getCode();
    assertNotEquals(entity.getCode(), newCode);

    service.verifyEmail(email, newCode);
    assertEquals(0, emailAuthenticationCodeRepository.count());
  }
}
