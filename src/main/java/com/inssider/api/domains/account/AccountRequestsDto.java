package com.inssider.api.domains.account;

import com.inssider.api.domains.account.AccountDataTypes.RegisterType;

public class AccountRequestsDto {
  public record Register(RegisterType registerType, String email, String password) {}

  public record ChangePassword(Long id, String password) {}
}
