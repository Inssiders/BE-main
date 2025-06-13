package com.inssider.api.domains.auth.code;

import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailChallengeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailVerifyResponse;
import java.util.UUID;

public interface AuthCodeService {

  AuthEmailChallengeResponse challengeEmail(String email);

  AuthEmailVerifyResponse verifyEmail(String email, String otp);

  AuthorizationCode redeem(UUID authCodeId);
}
