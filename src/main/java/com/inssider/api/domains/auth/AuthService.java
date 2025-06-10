package com.inssider.api.domains.auth;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.auth.AuthRequestsDto.LoginRequest;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailCodeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailVerificationResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.TokenResponse;

public interface AuthService {

  EmailCodeResponse challengeEmail(String email);

  EmailVerificationResponse verifyEmail(String email, String otp);

  TokenResponse createTokens(LoginRequest request);

  void revokeRefreshToken(Account account);
}
