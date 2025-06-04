package com.inssider.api.domains.account;

import java.util.Date;

public class AccountResponsesDto {
  public record PatchPassword(Date updatedAt) {}

  public record AccountCreated(String email, Date createdAt) {}
}
