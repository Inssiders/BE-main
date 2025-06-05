package com.inssider.api.domains.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.inssider.api.common.Util;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountRequestsDto.RegisterRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class AccountControllerTest {

  @Autowired private AccountController accountController;

  @Test
  @Transactional
  void 회원가입_요청() {
    Account account = Util.accountGenerator().get();
    RegisterRequestDto request =
        new RegisterRequestDto(RegisterType.PASSWORD, account.getEmail(), account.getPassword());
    var res = accountController.register(request);
    assertEquals(201, res.getStatusCode().value());
    assertEquals(account.getEmail(), res.getBody().data().email());
  }
}
