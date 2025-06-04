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

  public record EmailChallengeRequest(String email) {}

  public record EmailVerifyRequest(String email, String otp) {}

  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.PROPERTY,
      property = "grantType",
      visible = true)
  @JsonSubTypes({
    @JsonSubTypes.Type(value = PasswordLoginRequest.class, name = "PASSWORD"),
    @JsonSubTypes.Type(value = RefreshTokenLoginRequest.class, name = "REFRESH_TOKEN"),
    @JsonSubTypes.Type(value = AuthorizationCodeLoginRequest.class, name = "EMAIL")
  })
  public sealed interface LoginRequest
      permits PasswordLoginRequest, RefreshTokenLoginRequest, AuthorizationCodeLoginRequest {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    GrantType grantType();
  }

  public record PasswordLoginRequest(
      @Schema(allowableValues = GRANT_TYPE_PASSWORD) GrantType grantType,
      String email,
      String password)
      implements LoginRequest {}

  public record RefreshTokenLoginRequest(
      @Schema(allowableValues = GRANT_TYPE_REFRESH_TOKEN) GrantType grantType,
      String refreshToken,
      String clientId)
      implements LoginRequest {}

  public record AuthorizationCodeLoginRequest(
      @Schema(allowableValues = GRANT_TYPE_AUTHORIZATION_CODE) GrantType grantType, UUID uuid)
      implements LoginRequest {}
}
