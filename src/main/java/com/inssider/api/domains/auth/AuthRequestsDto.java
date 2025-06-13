package com.inssider.api.domains.auth;

import static com.inssider.api.domains.auth.AuthDataTypes.GRANT_TYPE_AUTHORIZATION_CODE;
import static com.inssider.api.domains.auth.AuthDataTypes.GRANT_TYPE_PASSWORD;
import static com.inssider.api.domains.auth.AuthDataTypes.GRANT_TYPE_REFRESH_TOKEN;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public class AuthRequestsDto {

  public record AuthEmailChallengeRequest(String email) {}

  public record AuthEmailVerifyRequest(String email, String otp) {}

  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      property = "grant_type",
      include = JsonTypeInfo.As.PROPERTY,
      visible = true)
  @JsonSubTypes({
    @JsonSubTypes.Type(value = AuthTokenWithPasswordRequest.class, name = GRANT_TYPE_PASSWORD),
    @JsonSubTypes.Type(
        value = AuthTokenWithRefreshTokenRequest.class,
        name = GRANT_TYPE_REFRESH_TOKEN),
    @JsonSubTypes.Type(
        value = AuthTokenWithAuthorizationCodeRequest.class,
        name = GRANT_TYPE_AUTHORIZATION_CODE)
  })
  public sealed interface AuthTokenRequest
      permits AuthTokenWithPasswordRequest,
          AuthTokenWithRefreshTokenRequest,
          AuthTokenWithAuthorizationCodeRequest {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    GrantType grantType();
  }

  public record AuthTokenWithPasswordRequest(GrantType grantType, String email, String password)
      implements AuthTokenRequest {}

  public record AuthTokenWithRefreshTokenRequest(
      GrantType grantType, String refreshToken, String clientId) implements AuthTokenRequest {}

  public record AuthTokenWithAuthorizationCodeRequest(GrantType grantType, UUID uuid)
      implements AuthTokenRequest {}
}
