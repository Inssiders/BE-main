package com.inssider.api.domains.account;

import com.inssider.api.common.Util;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class AccountRequestsDto {
  public record RegisterRequestDto(
      RegisterType registerType,
      @Email(regexp = Util.EMAIL_REGEX) String email,
      @Pattern(regexp = Util.PASSWORD_REGEX) String password) {}

  public record ChangePasswordRequestDto(@Pattern(regexp = Util.PASSWORD_REGEX) String password) {}
}
