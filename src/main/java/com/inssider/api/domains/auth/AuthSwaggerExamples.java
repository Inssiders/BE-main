package com.inssider.api.domains.auth;

class AuthSwaggerExamples {
  static class LoginExamples {
    // Password Login
    static final String PASSWORD_NAME = "PASSWORD";
    static final String PASSWORD_SUMMARY = "비밀번호 인증";
    static final String PASSWORD_VALUE =
        """
        {
        "grantType": "PASSWORD",
        "email": "user@example.com",
        "password": "mypassword123"
        }
        """;
    // Refresh Token Login
    static final String REFRESH_TOKEN_NAME = "REFRESH_TOKEN";
    static final String REFRESH_TOKEN_SUMMARY = "리프레시 토큰 로그인";
    static final String REFRESH_TOKEN_VALUE =
        """
        {
        "grantType": "REFRESH_TOKEN",
        "refreshToken": "your-refresh-token",
        "clientId": "inssider-app"
        }
        """;
    // Authorization Code Login
    static final String AUTHORIZATION_CODE_NAME = "AUTHORIZATION_CODE";
    static final String AUTHORIZATION_CODE_SUMMARY = "인가 코드 로그인";
    static final String AUTHORIZATION_CODE_VALUE =
        """
        {
        "grantType": "AUTHORIZATION_CODE",
        "uuid": "your-uuid"
        }
        """;
  }
}
