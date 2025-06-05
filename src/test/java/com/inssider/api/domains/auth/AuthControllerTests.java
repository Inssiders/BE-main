package com.inssider.api.domains.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.inssider.api.common.Util;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountService;
import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import com.inssider.api.domains.auth.AuthRequestsDto.EmailChallengeRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.EmailVerifyRequest;
import com.inssider.api.domains.auth.code.email.EmailAuthCode;
import com.inssider.api.domains.auth.code.email.EmailAuthService;
import com.inssider.api.domains.auth.token.JwtService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

  @Autowired private AuthController controller;

  @Autowired private AccountService accountService;
  @Autowired private EmailAuthService emailAuthService;
  @Autowired private EmailAuthService emailAuthorizationService;
  @Autowired private JwtService jwtService;

  @Test
  @Transactional
  void 로그인() {
    // 0. 계정 생성 (테스트용)
    var account = createTestAccount();
    var email = account.getEmail();
    assertEquals(1, accountService.count());

    // 1. 이메일 인증 요청
    {
      var request = new EmailChallengeRequest(email);
      controller.challengeEmailAuth(request);
    }
    assertEquals(1, emailAuthorizationService.countEmailCodes()); // 이메일 인증 코드 생성

    // 1.5 이메일 인증 코드 획득
    String code =
        emailAuthService
            .findById(email)
            .map(EmailAuthCode::getCode)
            .orElseThrow(() -> new AssertionError("Email code not found"));

    // 2. 이메일 인증 확인 및 authorization code 획득
    UUID authorizationCode;
    {
      var request = new EmailVerifyRequest(email, code);
      var response = controller.verifyEmailAuth(request).getBody().data();
      authorizationCode = response.authorization_code();
    }
    assertNotNull(authorizationCode);
    assertEquals(0, emailAuthorizationService.countEmailCodes()); // 인증 코드 사용 후 삭제
    assertEquals(1, jwtService.countAuthorizationCodes()); // authorization code 생성 확인

    // 3. authorization code로 토큰 생성
    String accessToken;
    String refreshToken;
    {
      var request =
          new AuthRequestsDto.AuthorizationCodeLoginRequest(
              GrantType.AUTHORIZATION_CODE, authorizationCode);
      var response = controller.createToken(request).getBody().data();
      accessToken = response.accessToken();
      refreshToken = response.refreshToken();
    }
    assertNotNull(accessToken);
    assertNull(refreshToken);
  }

  private Account createTestAccount() {
    var account = Util.accountGenerator().get();
    return accountService.register(account);
  }
}
