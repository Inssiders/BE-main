package com.inssider.api.domains.account;

import java.time.LocalDateTime;

public class AccountResponsesDto {

  public record PostAccountResponse(String email, LocalDateTime createdAt) {}

  public record PatchAccountMePasswordResponse(LocalDateTime updatedAt) {}
}
