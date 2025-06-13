package com.inssider.api.domains.account;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inssider.api.common.TestScenarioHelper;
import com.inssider.api.common.Util;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenWithPasswordRequest;
import com.inssider.api.domains.auth.AuthService;
import com.inssider.api.domains.profile.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestScenarioHelper.class)
class AccountControllerTest {
  @Autowired private TestScenarioHelper helper;
  @Autowired private AccountService accountService;
  @Autowired private AccountRepository accountRepository;

  @Autowired private UserProfileService userProfileService;
  @Autowired private AuthService authService;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @Transactional
  void 회원가입() throws Exception {
    // given
    Account account = Util.accountGenerator().get();
    String accessToken = helper.getSingleAccessToken(account.getEmail());

    // when
    assertDoesNotThrow(
        () -> helper.postAccount(account.getEmail(), account.getPassword(), accessToken));

    // then
    assertEquals(1, accountRepository.count());
  }

  @Test
  @Transactional
  void 중복_회원가입_실패() throws Exception {
    // 이메일 중복 확인으로 대체
  }

  @Test
  @Transactional
  void 회원탈퇴() throws Exception {
    // given
    Account account = Util.accountGenerator().get();
    String email = account.getEmail();
    String rawPassword = account.getPassword();
    Long accountId = register(account).getId();
    String accessToken = helper.getAccessToken(accountId);

    // when
    {
      mockMvc
          .perform(delete("/api/accounts/me").header("Authorization", "Bearer " + accessToken))
          .andExpect(status().isOk());
    }

    // then
    assertEquals(0, accountService.count());
    assertEquals(1, accountRepository.findAllDeleted().size());
    assertEquals(0, userProfileService.count());

    // 5. 회원 탈퇴 후, 로그인 시도 시 실패 then
    {
      var request = new AuthTokenWithPasswordRequest(GrantType.PASSWORD, email, rawPassword);
      mockMvc
          .perform(
              post("/api/auth/token")
                  .contentType("application/json")
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().is4xxClientError());
    }

    // 6. 재가입 시, 기존 계정 hard delete 후 계정 생성 확인 then
    {
      accountService.register(RegisterType.PASSWORD, email, rawPassword);
    }
    assertEquals(0, accountRepository.findAllDeleted().size());
    assertEquals(1, accountRepository.findAllIncludeDeleted().size());
    assertEquals(1, accountService.count());
  }

  @Test
  @Transactional
  void 비밀번호_변경() throws Exception {
    // 0. 회원가입 요청 given
    String email;
    String rawPassword;
    Account account = Util.accountGenerator().get();
    {
      email = account.getEmail();
      rawPassword = account.getPassword();
      register(account);
    }
    assertEquals(1, accountService.count());

    // 1. 로그인 given
    String accessToken;
    {
      var request = new AuthTokenWithPasswordRequest(GrantType.PASSWORD, email, rawPassword);
      var response = authService.createTokens(request);
      accessToken = response.accessToken();
    }
    assertNotNull(accessToken);

    // 2. 비밀번호 변경 요청 when
    String newPassword = Util.passwordGenerator().get();
    {
      var request = new AccountRequestsDto.PatchAccountPasswordRequest(newPassword);
      mockMvc
          .perform(
              patch("/api/accounts/me/password")
                  .header("Authorization", "Bearer " + accessToken)
                  .contentType("application/json")
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andDo(print());
    }

    // 3. 변경된 비밀번호로 로그인 시도 then
    {
      var request = new AuthTokenWithPasswordRequest(GrantType.PASSWORD, email, newPassword);
      var response = authService.createTokens(request);
      assertNotNull(response.accessToken());
    }
  }

  private Account register(Account account) {
    return accountService.register(
        RegisterType.PASSWORD, account.getEmail(), account.getPassword());
  }
}
