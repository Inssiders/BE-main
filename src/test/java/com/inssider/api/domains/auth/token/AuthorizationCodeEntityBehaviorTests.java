package com.inssider.api.domains.auth.token;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.inssider.api.common.Util;
import com.inssider.api.domains.auth.code.AuthorizationCode;
import com.inssider.api.domains.auth.code.AuthorizationCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class AuthAuthorizationCodeEntityBehaviorTests {

  @Autowired private AuthorizationCodeService service;

  @Test
  @Transactional
  void 인가_코드_생성_및_저장() {
    var email = Util.emailGenerator().get();
    var entity = service.save(new AuthorizationCode(email));

    assertNotNull(entity.getId());
  }
}
