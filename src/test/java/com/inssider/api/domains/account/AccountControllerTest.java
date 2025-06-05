package com.inssider.api.domains.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inssider.api.common.Util;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountRequestsDto.ChangePasswordRequestDto;
import com.inssider.api.domains.account.AccountRequestsDto.RegisterRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private AccountService service;

  @Test
  @Transactional
  void register_ShouldReturnCreatedAccount() throws Exception {
    RegisterRequestDto request =
        new RegisterRequestDto(RegisterType.PASSWORD, "test@example.com", "password123");

    MvcResult result =
        mockMvc
            .perform(
                post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.email").value("test@example.com"))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    System.out.println("Register response: " + responseBody);
  }

  @Test
  @Transactional
  void deleteAccount_ShouldDeleteAccountAndReturnOk() throws Exception {
    // 먼저 테스트용 계정 생성
    createTestAccount();
    assertEquals(1, service.count());

    // [ ] /api/auth/token 엔드포인트를 통해 JWT 토큰을 생성해야 합니다.
    String token = "Bearer validToken"; // 실제로는 JWT 토큰을 생성해야 함

    mockMvc
        .perform(delete("/accounts/me").header("Authorization", token))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").doesNotExist());

    assertEquals(0, service.count());
  }

  @Test
  @Transactional
  void changePassword_ShouldPatchPasswordAndReturnAccount() throws Exception {
    // 먼저 테스트용 계정 생성
    var testAccount = createTestAccount();
    ChangePasswordRequestDto request =
        new ChangePasswordRequestDto(testAccount.getId(), "newPassword123");

    MvcResult result =
        mockMvc
            .perform(
                patch("/accounts/me/password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data").exists())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    System.out.println("Change password response: " + responseBody);
  }

  // ========== Helper Methods ==========

  private Account createTestAccount() {
    var entity = Util.accountGenerator().get();
    return service.register(entity);
  }
}
