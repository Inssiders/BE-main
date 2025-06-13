package com.inssider.api.domains.auth.code;

import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailChallengeResponse;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthEmailVerifyResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthCodeServiceImpl implements AuthCodeService {

  private final EmailAuthenticationCodeRepository emailCodeRepository;
  private final AuthorizationCodeRepository authorizationCodeRepository;
  private final EmailService emailService;

  @Override
  public AuthEmailChallengeResponse challengeEmail(String email) {
    emailCodeRepository.findById(email).ifPresent(emailCodeRepository::delete);
    var code = emailCodeRepository.save(email).getCode();

    emailService.sendSimpleMessage(
        email, "Email Verification Code", "Your verification code is: " + code);
    return new AuthEmailChallengeResponse(email, 300);
  }

  @Override
  public AuthEmailVerifyResponse verifyEmail(String email, String otp) {
    var entity =
        emailCodeRepository
            .findById(email)
            .orElseThrow(
                () -> new IllegalArgumentException("해당 이메일에 대한 인증 코드를 찾을 수 없습니다: " + email));

    if (LocalDateTime.now().isAfter(entity.getExpiredAt())) {
      throw new IllegalArgumentException("이메일 인증 코드가 만료되었습니다.");
    }

    if (!entity.getCode().equals(otp)) {
      throw new IllegalArgumentException("유효하지 않은 이메일 인증 코드입니다.");
    }

    emailCodeRepository.deleteById(email);

    // Create and save AuthorizationCode entity
    UUID authCode = authorizationCodeRepository.save(new AuthorizationCode(email)).getId();
    return new AuthEmailVerifyResponse(true, authCode);
  }

  public String consume(UUID authorizationCode) {
    AuthorizationCode entity =
        authorizationCodeRepository
            .findById(authorizationCode)
            .orElseThrow(
                () -> new IllegalArgumentException("해당 인증 코드를 찾을 수 없습니다: " + authorizationCode));
    String email = entity.getEmail();
    authorizationCodeRepository.deleteById(authorizationCode);
    return email;
  }

  @Override
  public AuthorizationCode redeem(UUID authCodeId) {
    return authorizationCodeRepository
        .findById(authCodeId)
        .map(
            entity -> {
              authorizationCodeRepository.delete(entity);
              return entity;
            })
        .orElseThrow(() -> new IllegalArgumentException("해당 인증 코드를 찾을 수 없습니다: " + authCodeId));
  }
}
