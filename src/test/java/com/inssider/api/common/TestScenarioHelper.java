package com.inssider.api.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountRequestsDto.PostAccountRequest;
import com.inssider.api.domains.account.AccountResponsesDto.PostAccountResponse;
import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthEmailVerifyRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenWithAuthorizationCodeRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenWithPasswordRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenWithRefreshTokenRequest;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailVerifyResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthTokenResponse;
import com.inssider.api.domains.auth.code.EmailAuthenticationCodeTestRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.web.servlet.MockMvc;

@TestComponent
public class TestScenarioHelper {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private JwtEncoder jwtEncoder;

  @Autowired private EmailAuthenticationCodeTestRepository emailCodeRepository;

  // 이메일 인증 요청 성공
  // POST /api/auth/email/challenge
  // request: AuthEmailChallengeRequest
  // response: String
  public String postAuthEmailChallenge(String email) {
    // 실제 이메일 전송 과정 생략
    emailCodeRepository.findById(email).ifPresent(emailCodeRepository::delete);
    return emailCodeRepository.save(email).getCode();
  }

  // 이메일 인증 검증 성공
  // POST /api/auth/email/verify
  // request: AuthEmailVerifyRequest
  // response: AuthEmailVerifyResponse
  public AuthEmailVerifyResponse postAuthEmailVerify(String email, String otp) throws Exception {
    var request = new AuthEmailVerifyRequest(email, otp);
    var response =
        mockMvc
            .perform(
                post("/api/auth/email/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var data = objectMapper.readTree(response).get("data");
    return objectMapper.treeToValue(data, AuthEmailVerifyResponse.class);
  }

  // 인가 코드 기반 일회용 로그인 성공
  // POST /api/auth/token
  // request: AuthTokenWithAuthorizationCodeRequest
  // response: AuthTokenResponse
  public AuthTokenResponse postAuthTokenWithAuthorizationCode(UUID uuid) throws Exception {
    var request = new AuthTokenWithAuthorizationCodeRequest(GrantType.AUTHORIZATION_CODE, uuid);
    var response =
        mockMvc
            .perform(
                post("/api/auth/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();
    var data = objectMapper.readTree(response).get("data");
    return objectMapper.treeToValue(data, AuthTokenResponse.class);
  }

  // 이메일 패스워드 기반 로그인 성공
  // POST /api/auth/token
  // request: AuthTokenWithPasswordRequest
  // response: AuthTokenResponse
  public AuthTokenResponse postAuthTokenWithPassword(String email, String password)
      throws Exception {
    var request = new AuthTokenWithPasswordRequest(GrantType.PASSWORD, email, password);
    var response =
        mockMvc
            .perform(
                post("/api/auth/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();
    var data = objectMapper.readTree(response).get("data");
    return objectMapper.treeToValue(data, AuthTokenResponse.class);
  }

  // 리프레쉬 토큰 기반 재발급 성공
  // POST /api/auth/token
  // request: AuthTokenWithRefreshTokenRequest
  // response: AuthTokenResponse
  public AuthTokenResponse postAuthTokenWithRefreshToken(String refreshToken) throws Exception {
    var request =
        new AuthTokenWithRefreshTokenRequest(GrantType.REFRESH_TOKEN, refreshToken, "inssider-app");
    var response =
        mockMvc
            .perform(
                post("/api/auth/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();
    var data = objectMapper.readTree(response).get("data");
    return objectMapper.treeToValue(data, AuthTokenResponse.class);
  }

  // 회원가입 성공
  // POST /api/accounts
  // Header: Authorization: Bearer {accessToken}
  // request: PostAccountRequest
  // response: PostAccountResponse
  public PostAccountResponse postAccount(String email, String password, String accessToken)
      throws Exception {
    var request = new PostAccountRequest(RegisterType.PASSWORD, email, password);
    var response =
        mockMvc
            .perform(
                post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();
    var data = objectMapper.readTree(response).get("data");
    return objectMapper.treeToValue(data, PostAccountResponse.class);
  }

  public String getAccessToken(Long accountId) {
    Instant now = Instant.now();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("api.inssider.com")
            .issuedAt(now)
            .audience(List.of("inssider-app"))
            .subject(String.valueOf(accountId))
            .expiresAt(now.plus(600, ChronoUnit.SECONDS))
            .claim("type", "access")
            .id(UUID.randomUUID().toString())
            .build();
    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  public String getSingleAccessToken(String email) {
    Instant now = Instant.now();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("api.inssider.com")
            .issuedAt(now)
            .audience(List.of("inssider-app"))
            .subject(email)
            .expiresAt(now.plus(600, ChronoUnit.SECONDS))
            .claim("type", "single_access")
            .id(UUID.randomUUID().toString())
            .build();
    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
