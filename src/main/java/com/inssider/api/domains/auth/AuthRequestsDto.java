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
      include = JsonTypeInfo.As.PROPERTY,
      property = "grantType",
      visible = true)
  @JsonSubTypes({
    @JsonSubTypes.Type(value = AuthTokenWithPasswordRequest.class, name = "PASSWORD"),
    @JsonSubTypes.Type(value = AuthTokenWithRefreshTokenRequest.class, name = "REFRESH_TOKEN"),
    @JsonSubTypes.Type(value = AuthTokenWithAuthorizationCodeRequest.class, name = "EMAIL")
  })
  public sealed interface AuthTokenRequest
      permits AuthTokenWithPasswordRequest,
          AuthTokenWithRefreshTokenRequest,
          AuthTokenWithAuthorizationCodeRequest {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    GrantType grantType();
  }

  public record AuthTokenWithPasswordRequest(
      @Schema(allowableValues = GRANT_TYPE_PASSWORD) GrantType grantType,
      String email,
      String password)
      implements AuthTokenRequest {}

  public record AuthTokenWithRefreshTokenRequest(
      @Schema(allowableValues = GRANT_TYPE_REFRESH_TOKEN) GrantType grantType,
      String refreshToken,
      String clientId)
      implements AuthTokenRequest {}

  public record AuthTokenWithAuthorizationCodeRequest(
      @Schema(allowableValues = GRANT_TYPE_AUTHORIZATION_CODE) GrantType grantType, UUID uuid)
      implements AuthTokenRequest {}
}
