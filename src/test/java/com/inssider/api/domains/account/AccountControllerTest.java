package com.inssider.api.domains.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.inssider.api.common.Util;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountRequestsDto.RegisterRequestDto;
import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import com.inssider.api.domains.auth.AuthRequestsDto.PasswordLoginRequest;
import com.inssider.api.domains.auth.AuthService;
import com.inssider.api.domains.profile.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class AccountControllerTest {

  @Autowired private AccountController accountController;
  @Autowired private AccountService accountService;
  @Autowired private AccountRepository accountRepository;

  @Autowired private UserProfileService userProfileService;
  @Autowired private AuthService authService;

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

  @Test
  @Transactional
  void 회원탈퇴() {
    // 0. 회원가입 요청 given
    String email;
    String rawPassword;
    {
      Account account = Util.accountGenerator().get();
      email = account.getEmail();
      rawPassword = account.getPassword();
      accountService.register(account);
    }
    assertEquals(1, accountService.count());

    // 1. 로그인 given
    String accessToken;
    {
      var request = new PasswordLoginRequest(GrantType.PASSWORD, email, rawPassword);
      var response = authService.createToken(request);
      accessToken = response.accessToken();
    }
    assertNotNull(accessToken);

    // 3. 회원 탈퇴 요청 when
    {
      var response = accountController.deleteAccount(accessToken);
      assertEquals(200, response.getStatusCode().value());
    }
    assertEquals(0, accountService.count());
    assertEquals(1, accountRepository.findAllDeleted().size());
    assertEquals(0, userProfileService.count());

    // 4. soft-delete 된 계정 확인 then
    // 5. 회원 탈퇴 후, 로그인 시도 시 실패 then
    // 6. 재가입 시, 기존 계정 hard delete 후 계정 생성 확인 then
    // 2. header Authorization 토큰 given
    // 3. 회원 탈퇴 요청 when
    // 4. soft-delete 된 계정 확인 then
    // 5. 회원 탈퇴 후, 로그인 시도 시 실패 then
    // 6. 재가입 시, 기존 계정 hard delete 후 계정 생성 확인 then
  }
}
