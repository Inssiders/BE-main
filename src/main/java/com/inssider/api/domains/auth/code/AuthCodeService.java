package com.inssider.api.domains.auth.code;

import com.inssider.api.domains.auth.AuthResponsesDto.EmailCodeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailVerificationResponse;
import java.util.UUID;

public interface AuthCodeService {

  EmailCodeResponse challengeEmail(String email);

  EmailVerificationResponse verifyEmail(String email, String code);

  AuthorizationCode consume(UUID authorizationCode);
}
