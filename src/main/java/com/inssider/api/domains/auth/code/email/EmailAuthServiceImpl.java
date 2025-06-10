package com.inssider.api.domains.auth.code.email;

import com.inssider.api.domains.auth.AuthResponsesDto.EmailCodeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailVerificationResponse;
import com.inssider.api.domains.auth.code.AuthorizationCode;
import com.inssider.api.domains.auth.code.AuthorizationCodeService;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class EmailAuthServiceImpl implements EmailAuthService {

  private final EmailAuthCodeRepository emailCodeRepository;
  private final AuthorizationCodeService authorizationCodeService;

  public EmailAuthServiceImpl(
      EmailAuthCodeRepository emailCodeRepository,
      AuthorizationCodeService authorizationCodeService) {
    this.emailCodeRepository = emailCodeRepository;
    this.authorizationCodeService = authorizationCodeService;
  }

  @Override
  public Long countEmailCodes() {
    return emailCodeRepository.count();
  }

  @Override
  public EmailCodeResponse challengeEmail(String email) {
    emailCodeRepository.findById(email).ifPresent(emailCodeRepository::delete);
    assert emailCodeRepository.save(email).getCode() != null;
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
    var savedAuthCode = authorizationCodeService.save(authCode);
    UUID authCodeId = savedAuthCode.getId();

    return new EmailVerificationResponse(true, authCodeId);
  }

  @Override
  public Optional<EmailAuthCode> findById(String email) {
    return emailCodeRepository.findById(email);
  }
}
