package com.inssider.api.domains.auth.code.email;

import com.inssider.api.domains.auth.AuthResponsesDto.EmailCodeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailVerificationResponse;
import java.util.Optional;

public interface EmailAuthService {

  Long countEmailCodes();

  EmailCodeResponse challengeEmail(String email);

  EmailVerificationResponse verifyEmail(String email, String code);

  Optional<EmailAuthCode> findById(String email);
}
