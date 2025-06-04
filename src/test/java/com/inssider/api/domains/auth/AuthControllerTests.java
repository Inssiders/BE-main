package com.inssider.api.domains.auth;

import static com.inssider.api.domains.auth.AuthDataTypes.GrantType.AUTHORIZATION_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inssider.api.common.Util;
import com.inssider.api.domains.account.AccountService;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthorizationCodeLoginRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.EmailChallengeRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.EmailVerifyRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.LoginRequest;
import com.inssider.api.domains.auth.code.email.EmailAuthService;
import com.inssider.api.domains.auth.token.JwtService;
import java.util.UUID;
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
public class AuthControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private static final String TEST_CODE = "123456";

  @Autowired private AccountService accountService;

  @Autowired private EmailAuthService emailAuthorizationService;
  @Autowired private JwtService jwtService;

  @Test
  @Transactional
  void testEmailAuthFlow() throws Exception {
    // 0. 계정 생성 (테스트용)
    var email = createTestAccount();
    assertEquals(1, accountService.count());

    // 1. 이메일 인증 요청
    requestEmailChallenge(email);
    assertEquals(1, emailAuthorizationService.countEmailCodes());

    // 2. 이메일 인증 확인 및 authorization code 획득
    UUID authorizationCode = verifyEmailAndGetAuthCode(email, TEST_CODE);
    assertEquals(0, emailAuthorizationService.countEmailCodes());
    assertEquals(1, jwtService.countAuthorizationCodes());

    // 3. authorization code로 토큰 생성
    String accessToken = createTokenWithAuthCode(authorizationCode);
    assertNotNull(accessToken);
  }

  // ========== Helper Methods ==========

  private String createTestAccount() {
    var entity = Util.accountGenerator().get();
    accountService.register(entity);
    return entity.getEmail();
  }

  /** 이메일 인증 코드 요청 */
  private void requestEmailChallenge(String email) throws Exception {
    EmailChallengeRequest request = new EmailChallengeRequest(email);

    MvcResult result =
        mockMvc
            .perform(
                post("/api/auth/email/challenge")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.email").value(email))
            .andReturn();

    // 실제 응답 내용을 출력해서 구조 확인
    String responseBody = result.getResponse().getContentAsString();
    System.out.println("Email challenge response: " + responseBody);
  }

  /** 이메일 인증 확인 및 authorization code 반환 */
  private UUID verifyEmailAndGetAuthCode(String email, String code) throws Exception {
    EmailVerifyRequest request = new EmailVerifyRequest(email, code);

    MvcResult result =
        mockMvc
            .perform(
                post("/api/auth/email/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.data.authorization_code").exists())
            .andReturn();

    // JSON 응답에서 authorization_code 추출
    String responseBody = result.getResponse().getContentAsString();
    JsonNode jsonNode = objectMapper.readTree(responseBody);
    return UUID.fromString(jsonNode.get("data").get("authorization_code").asText());
  }

  /** Authorization code로 액세스 토큰 생성 */
  private String createTokenWithAuthCode(UUID authCode) throws Exception {
    LoginRequest request = new AuthorizationCodeLoginRequest(AUTHORIZATION_CODE, authCode);

    MvcResult result =
        mockMvc
            .perform(
                post("/api/auth/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.access_token").exists())
            .andExpect(
                jsonPath("$.data.refresh_token").value((String) null)) // Refresh token은 null로 설정
            .andExpect(jsonPath("$.data.token_type").value("Bearer"))
            .andExpect(jsonPath("$.data.expires_in").exists())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    JsonNode jsonNode = objectMapper.readTree(responseBody);
    JsonNode dataNode = jsonNode.get("data");

    String accessToken = dataNode.get("access_token").asText();
    return accessToken;
  }
}
