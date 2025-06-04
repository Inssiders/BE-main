package com.inssider.api.domains.auth;

import java.util.UUID;

public class AuthResponsesDto {
  public record EmailCodeResponse(String email, int expiresIn) {}

  public record EmailVerificationResponse(boolean verified, UUID authorization_code) {}

  public record TokenResponse(
      String accessToken, String refreshToken, String tokenType, Long expiresIn) {}
}
