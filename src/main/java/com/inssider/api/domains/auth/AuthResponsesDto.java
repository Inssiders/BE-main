package com.inssider.api.domains.auth;

import java.util.UUID;

public class AuthResponsesDto {
  public record AuthEmailChallengeResponse(String email, int expiresIn) {}

  public record AuthEmailVerifyResponse(boolean verified, UUID authorization_code) {}

  public record AuthTokenResponse(
      String accessToken, String refreshToken, String tokenType, Long expiresIn) {}
}
