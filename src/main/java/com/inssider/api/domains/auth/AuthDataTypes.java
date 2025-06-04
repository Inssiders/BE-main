package com.inssider.api.domains.auth;

public class AuthDataTypes {
  public static final String GRANT_TYPE_AUTHORIZATION_CODE = "AUTHORIZATION_CODE";
  public static final String GRANT_TYPE_PASSWORD = "PASSWORD";
  public static final String GRANT_TYPE_REFRESH_TOKEN = "REFRESH_TOKEN";

  public enum GrantType {
    PASSWORD,
    REFRESH_TOKEN,
    AUTHORIZATION_CODE;

    public boolean isPassword() {
      return this.name().equals(GRANT_TYPE_PASSWORD);
    }

    public boolean isRefreshToken() {
      return this.name().equals(GRANT_TYPE_REFRESH_TOKEN);
    }

    public boolean isAuthorizationCode() {
      return this.name().equals(GRANT_TYPE_AUTHORIZATION_CODE);
    }
  }
}
