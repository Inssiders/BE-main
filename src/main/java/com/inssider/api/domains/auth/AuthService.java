package com.inssider.api.domains.auth;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.auth.AuthRequestsDto.AuthTokenRequest;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailChallengeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailVerifyResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthTokenResponse;

public interface AuthService {

  AuthEmailChallengeResponse challengeEmail(String email);

  AuthEmailVerifyResponse verifyEmail(String email, String otp);

  AuthTokenResponse createTokens(AuthTokenRequest request);

  void revokeRefreshToken(Account account);
}
