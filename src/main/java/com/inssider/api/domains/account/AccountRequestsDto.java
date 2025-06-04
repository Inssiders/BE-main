package com.inssider.api.domains.account;

import com.inssider.api.domains.account.AccountDataTypes.RegisterType;

public class AccountRequestsDto {
  public record RegisterRequestDto(RegisterType registerType, String email, String password) {}

  public record ChangePasswordRequestDto(Long id, String password) {}
}
