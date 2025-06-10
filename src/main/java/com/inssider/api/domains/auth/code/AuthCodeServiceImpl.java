package com.inssider.api.domains.auth.code;

import com.inssider.api.domains.auth.AuthResponsesDto.EmailCodeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailVerificationResponse;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
class AuthCodeServiceImpl implements AuthCodeService {

  private final EmailAuthenticationCodeRepository emailCodeRepository;
  private final AuthorizationCodeRepository authorizationCodeRepository;

  @Override
  public EmailCodeResponse challengeEmail(String email) {
    emailCodeRepository.findById(email).ifPresent(emailCodeRepository::delete);
    Assert.notNull(emailCodeRepository.save(email).getCode(), "Email code should not be null");
    return new EmailCodeResponse(email, 300);
  }

  @Override
  public EmailVerificationResponse verifyEmail(@NonNull String email, @NonNull String code) {
    // [ ] check if the code is valid and not expired
    // var actualCode = emailCodeRepository.findById(email).orElseThrow().getCode();
    // boolean verified = code.equals(actualCode);

    boolean verified = true; // Assume verification is always successful
    if (!verified) {
      throw new IllegalArgumentException("Invalid or expired email verification code");
    }

    emailCodeRepository.deleteById(email);

    // Create and save AuthorizationCode entity
    var authCode = new AuthorizationCode(email);
    var savedAuthCode = authorizationCodeRepository.save(authCode);
    UUID authCodeId = savedAuthCode.getId();

    return new EmailVerificationResponse(true, authCodeId);
  }

  @Override
  public AuthorizationCode consume(UUID authorizationCode) {
    var entity = authorizationCodeRepository.findById(authorizationCode).orElseThrow();
    authorizationCodeRepository.delete(entity);
    return entity;
  }
}
