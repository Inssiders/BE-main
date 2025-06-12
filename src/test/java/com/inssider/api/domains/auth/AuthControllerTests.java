package com.inssider.api.domains.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inssider.api.common.Util;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountService;
import com.inssider.api.domains.account.AccountTestRepository;
import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthEmailChallengeRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthEmailVerifyRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenWithPasswordRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenWithRefreshTokenRequest;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthTokenResponse;
import com.inssider.api.domains.auth.code.AuthorizationCodeTestRepository;
import com.inssider.api.domains.auth.code.EmailAuthenticationCodeTestRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests {

  @Autowired private AuthController controller;

  // auth-sub
  @Autowired private EmailAuthenticationCodeTestRepository emailAuthenticationCodeRepository;
  @Autowired private AuthorizationCodeTestRepository authorizationCodeRepository;

  // common
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  // account
  @Autowired private AccountService accountService;
  @Autowired AccountTestRepository accountRepository;

  @Test
  @Transactional
  void 이메일_인증() {
    // 0. 계정 생성 (테스트용)
    var account = Util.accountGenerator().get();
    var email = account.getEmail();
    register(account);
    assertEquals(1, accountService.count());

    // 1. 이메일 인증 요청
    {
      var request = new AuthEmailChallengeRequest(email);
      controller.challengeEmailAuth(request);
    }
    assertEquals(1, emailAuthenticationCodeRepository.count());

    // 2. 이메일 인증 코드 획득
    String emailCode = emailAuthenticationCodeRepository.findAll().getFirst().getCode();

    // 3. 이메일 인증 확인 및 authorization code 획득
    UUID authorizationCode;
    {
      var request = new AuthEmailVerifyRequest(email, emailCode);
      var response = controller.verifyEmailAuth(request).getBody().data();
      authorizationCode = response.authorization_code();
    }
    assertNotNull(authorizationCode);
    assertEquals(0, emailAuthenticationCodeRepository.count()); // 인증 코드 사용 후 삭제
    assertEquals(1, authorizationCodeRepository.count()); // authorization code 생성 확인

    // 4. authorization code로 토큰 생성
    {
      var request =
          new AuthRequestsDto.AuthTokenWithAuthorizationCodeRequest(
              GrantType.AUTHORIZATION_CODE, authorizationCode);
      var response = controller.createToken(request).getBody().data();
      assertNotNull(response.accessToken());
      assertNull(response.refreshToken());
    }
  }

  @Test
  @Transactional
  void 로그인() {
    // 0. 계정 생성 (테스트용)
    var account = Util.accountGenerator().get();
    var email = account.getEmail();
    var plainPassword = account.getPassword();
    register(account);
    assertFalse(plainPassword.matches("\\{.+\\}$"));
    assertEquals(1, accountService.count());

    // 1. 로그인 요청
    {
      var request = new AuthTokenWithPasswordRequest(GrantType.PASSWORD, email, plainPassword);
      var response = controller.createToken(request).getBody().data();
      assertNotNull(response.accessToken());
      assertNotNull(response.refreshToken());
    }

    // 2. 로그인 성공 후, refresh_token 저장 여부
    {
      account = accountRepository.findByEmail(email).orElseThrow();
      assertNotNull(account.getRefreshToken());
    }
  }

  @Test
  @Transactional
  void 로그아웃() throws Exception {
    // 0. 계정 생성 (테스트용)
    var account = Util.accountGenerator().get();
    var email = account.getEmail();
    var plainPassword = account.getPassword();
    register(account);
    assertFalse(plainPassword.matches("\\{.+\\}$"));
    assertEquals(1, accountService.count());

    // 1. 로그인 요청
    String accessToken;
    {
      var request = new AuthTokenWithPasswordRequest(GrantType.PASSWORD, email, plainPassword);
      var response = controller.createToken(request).getBody().data();
      accessToken = response.accessToken();
    }
    assertNotNull(accessToken);

    // 2. 로그아웃 요청
    {
      mockMvc
          .perform(delete("/api/auth/token").header("Authorization", "Bearer " + accessToken))
          .andExpect(status().isOk());
    }
    assertNull(account.getRefreshToken()); // 로그아웃 -> refresh_token 삭제

    // 3. 로그아웃 후 이전 access_token으로 회원탈퇴 요청 시 예외 발생 확인
    // [ ] token temporal blacklist
    // {
    // mockMvc
    // .perform(delete("/api/accounts/me").header("Authorization", "Bearer " +
    // accessToken))
    // .andExpect(status().is4xxClientError());
    // }
  }

  @Test
  @Transactional
  void 토큰_재발급() throws Exception {
    // 0. 계정 생성 (테스트용)
    var account = Util.accountGenerator().get();
    var email = account.getEmail();
    var plainPassword = account.getPassword();
    register(account);
    assertFalse(plainPassword.matches("\\{.+\\}$"));
    assertEquals(1, accountService.count());

    // 1. 로그인 요청
    String accessToken;
    String refreshToken;
    {
      var request = new AuthTokenWithPasswordRequest(GrantType.PASSWORD, email, plainPassword);
      var response = controller.createToken(request).getBody().data();
      accessToken = response.accessToken();
      refreshToken = response.refreshToken();
    }
    assertNotNull(accessToken);
    assertNotNull(refreshToken);

    // 2. 토큰 재발급 요청
    {
      var request =
          new AuthTokenWithRefreshTokenRequest(
              GrantType.REFRESH_TOKEN, refreshToken, "inssider-app");

      // var response = controller.createToken(request).getBody().data();
      var rawResponse =
          mockMvc
              .perform(
                  post("/api/auth/token")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andExpect(status().isOk())
              .andDo(print())
              .andReturn()
              .getResponse()
              .getContentAsString();
      AuthTokenResponse response = objectMapper.readValue(rawResponse, AuthTokenResponse.class);
      var newAccessToken = response.accessToken();
      var newRefreshToken = response.refreshToken();

      assertNotEquals(accessToken, newAccessToken);
      assertNotEquals(refreshToken, newRefreshToken);
    }
  }

  private Account register(Account account) {
    return accountService.register(
        RegisterType.PASSWORD, account.getEmail(), account.getPassword());
  }
}
