package com.inssider.api.domains.auth;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.common.response.BaseResponse.ResponseWrapper;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthEmailChallengeRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthEmailVerifyRequest;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenRequest;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailChallengeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailVerifyResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthTokenResponse;
import com.inssider.api.domains.auth.AuthSwaggerExamples.LoginExamples;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
class AuthController {

  private final AuthService authService;

  // 이메일 인증코드 발송
  @PostMapping("/email/challenge")
  public ResponseEntity<ResponseWrapper<AuthEmailChallengeResponse>> challengeEmailAuth(
      @RequestBody AuthEmailChallengeRequest request) {
    var body = authService.challengeEmail(request.email());
    return BaseResponse.of(200, body);
  }

  // 이메일 인증코드 검증
  @PostMapping("/email/verify")
  public ResponseEntity<ResponseWrapper<AuthEmailVerifyResponse>> verifyEmailAuth(
      @RequestBody AuthEmailVerifyRequest request) {
    var body = authService.verifyEmail(request.email(), request.otp());
    return BaseResponse.of(200, body);
  }

  // 일회용 로그인 / 토큰 발급 / 토큰 재발급
  @PostMapping("/token")
  public ResponseEntity<ResponseWrapper<AuthTokenResponse>> createToken(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              content =
                  @Content(
                      mediaType = "application/json",
                      examples = {
                        @ExampleObject(
                            name = LoginExamples.PASSWORD_NAME,
                            summary = LoginExamples.PASSWORD_SUMMARY,
                            value = LoginExamples.PASSWORD_VALUE),
                        @ExampleObject(
                            name = LoginExamples.REFRESH_TOKEN_NAME,
                            summary = LoginExamples.REFRESH_TOKEN_SUMMARY,
                            value = LoginExamples.REFRESH_TOKEN_VALUE),
                        @ExampleObject(
                            name = LoginExamples.AUTHORIZATION_CODE_NAME,
                            summary = LoginExamples.AUTHORIZATION_CODE_SUMMARY,
                            value = LoginExamples.AUTHORIZATION_CODE_VALUE),
                      }))
          @RequestBody
          AuthTokenRequest request) {
    return BaseResponse.of(200, authService.createTokens(request));
  }

  // 로그아웃
  @DeleteMapping("/token")
  public ResponseEntity<ResponseWrapper<Void>> revokeToken(
      @AuthenticationPrincipal Account account) {
    authService.revokeRefreshToken(account);
    return BaseResponse.of(200, null);
  }
}
