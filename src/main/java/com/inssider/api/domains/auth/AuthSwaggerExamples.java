package com.inssider.api.domains.auth;

import static com.inssider.api.domains.auth.AuthDataTypes.GRANT_TYPE_AUTHORIZATION_CODE;
import static com.inssider.api.domains.auth.AuthDataTypes.GRANT_TYPE_PASSWORD;
import static com.inssider.api.domains.auth.AuthDataTypes.GRANT_TYPE_REFRESH_TOKEN;

class AuthSwaggerExamples {
  static class LoginExamples {
    // Password Login
    static final String PASSWORD_NAME = GRANT_TYPE_PASSWORD;
    static final String PASSWORD_SUMMARY = "비밀번호 인증";
    static final String PASSWORD_VALUE =
        """
        {
        "grant_type": "PASSWORD",
        "email": "user@example.com",
        "password": "myp@22word"
        }
        """;
    // Refresh Token Login
    static final String REFRESH_TOKEN_NAME = GRANT_TYPE_REFRESH_TOKEN;
    static final String REFRESH_TOKEN_SUMMARY = "리프레시 토큰 로그인";
    static final String REFRESH_TOKEN_VALUE =
        """
        {
        "grant_type": "REFRESH_TOKEN",
        "refresh_token": "<Header>.<Payload>.<Signature>",
        "client_id": "inssider-app"
        }
        """;
    // Authorization Code Login
    static final String AUTHORIZATION_CODE_NAME = GRANT_TYPE_AUTHORIZATION_CODE;
    static final String AUTHORIZATION_CODE_SUMMARY = "인가 코드 로그인";
    static final String AUTHORIZATION_CODE_VALUE =
        """
        {
        "grant_type": "AUTHORIZATION_CODE",
        "uuid": "0ad9d20e-3e33-4a6b-bdf2-c1f930447bc2"
        }
        """;
  }
}
