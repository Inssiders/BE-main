package com.inssider.api.domains.auth.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inssider.api.common.TestScenarioHelper;
import com.inssider.api.common.Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestScenarioHelper.class)
class AuthCodeEntityBehaviorTests {

  @Autowired private AuthCodeService service;

  @Autowired private EmailAuthenticationCodeTestRepository emailAuthenticationCodeRepository;
  @Autowired private AuthorizationCodeTestRepository authorizationCodeRepository;

  @Autowired private TestScenarioHelper helper;

  @Test
  @Transactional
  void 이메일_인증코드_생명주기() {

    // 0. random email
    String email = Util.emailGenerator().get();

    // 1. first challenge
    helper.postAuthEmailChallenge(email);
    assertEquals(1, emailAuthenticationCodeRepository.count());
    EmailAuthenticationCode entity =
        emailAuthenticationCodeRepository.findById(email).orElseThrow();
    assertEquals(6, entity.getCode().length());
    assertEquals(entity.getCreatedAt().plusSeconds(300), entity.getExpiredAt());

    // 2. second challenge
    helper.postAuthEmailChallenge(email);
    assertEquals(1, emailAuthenticationCodeRepository.count());

    var newCode = emailAuthenticationCodeRepository.findById(email).orElseThrow().getCode();
    assertNotEquals(entity.getCode(), newCode);

    service.verifyEmail(email, newCode);
    assertEquals(0, emailAuthenticationCodeRepository.count());
  }

  @Test
  @Transactional
  void 인가_코드_자동_속성() {
    var entity = authorizationCodeRepository.save(new AuthorizationCode());
    assertNotNull(entity.getId());
    assertTrue(entity.getExpiredAt().isAfter(entity.getCreatedAt()));
  }
}
