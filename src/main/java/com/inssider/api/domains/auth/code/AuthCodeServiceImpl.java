package com.inssider.api.domains.auth.code;

import com.inssider.api.domains.auth.AuthResponsesDto.EmailCodeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.EmailVerificationResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthCodeServiceImpl implements AuthCodeService {

  private final EmailAuthenticationCodeRepository emailCodeRepository;
  private final AuthorizationCodeRepository authorizationCodeRepository;
  private final EmailService emailService;

  @Override
  public EmailCodeResponse challengeEmail(String email) {
    emailCodeRepository.findById(email).ifPresent(emailCodeRepository::delete);
    var code = emailCodeRepository.save(email).getCode();

    emailService.sendSimpleMessage(
        email, "Email Verification Code", "Your verification code is: " + code);
    return new EmailCodeResponse(email, 300);
  }

  @Override
  public EmailVerificationResponse verifyEmail(@NonNull String email, @NonNull String code) {
    var entity =
        emailCodeRepository
            .findById(email)
            .orElseThrow(
                () -> new IllegalArgumentException("해당 이메일에 대한 인증 코드를 찾을 수 없습니다: " + email));

    if (LocalDateTime.now().isAfter(entity.getExpiredAt())) {
      throw new IllegalArgumentException("이메일 인증 코드가 만료되었습니다.");
    }

    if (!entity.getCode().equals(code)) {
      throw new IllegalArgumentException("유효하지 않은 이메일 인증 코드입니다.");
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
