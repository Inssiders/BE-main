package com.inssider.api.domains.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inssider.api.common.Util;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountRequestsDto.RegisterRequestDto;
import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import com.inssider.api.domains.auth.AuthRequestsDto.PasswordLoginRequest;
import com.inssider.api.domains.auth.AuthService;
import com.inssider.api.domains.profile.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
  @Autowired private AccountController accountController;
  @Autowired private AccountService accountService;
  @Autowired private AccountRepository accountRepository;

  @Autowired private UserProfileService userProfileService;
  @Autowired private AuthService authService;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @Transactional
  void 회원가입() {
    Account account = Util.accountGenerator().get();
    RegisterRequestDto request =
        new RegisterRequestDto(RegisterType.PASSWORD, account.getEmail(), account.getPassword());
    var res = accountController.register(request);
    assertEquals(201, res.getStatusCode().value());
    assertEquals(account.getEmail(), res.getBody().data().email());
  }

  @Test
  @Transactional
  void 중복_회원가입_실패() throws Exception {
    Account account = Util.accountGenerator().get();
    RegisterRequestDto request =
        new RegisterRequestDto(RegisterType.PASSWORD, account.getEmail(), account.getPassword());
    var res = accountController.register(request);
    assertEquals(201, res.getStatusCode().value());

    mockMvc
        .perform(
            post("/api/accounts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void 회원탈퇴() throws Exception {
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
      var request = new PasswordLoginRequest(GrantType.PASSWORD, email, rawPassword);
      var response = authService.createTokens(request);
      accessToken = response.accessToken();
    }
    assertNotNull(accessToken);

    // 3. 회원 탈퇴 요청 when
    {
      mockMvc
          .perform(delete("/api/accounts/me").header("Authorization", "Bearer " + accessToken))
          .andExpect(status().isOk());
    }

    // 4. soft-delete 된 계정 확인 then
    assertEquals(0, accountService.count());
    assertEquals(1, accountRepository.findAllDeleted().size());
    assertEquals(0, userProfileService.count());

    // 5. 회원 탈퇴 후, 로그인 시도 시 실패 then
    {
      var request = new PasswordLoginRequest(GrantType.PASSWORD, email, rawPassword);
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
      var request = new PasswordLoginRequest(GrantType.PASSWORD, email, rawPassword);
      var response = authService.createTokens(request);
      accessToken = response.accessToken();
    }
    assertNotNull(accessToken);

    // 2. 비밀번호 변경 요청 when
    String newPassword = Util.passwordGenerator().get();
    {
      var request = new AccountRequestsDto.ChangePasswordRequestDto(newPassword);
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
      var request = new PasswordLoginRequest(GrantType.PASSWORD, email, newPassword);
      var response = authService.createTokens(request);
      assertNotNull(response.accessToken());
    }
  }

  private Account register(Account account) {
    return accountService.register(
        RegisterType.PASSWORD, account.getEmail(), account.getPassword());
  }
}
