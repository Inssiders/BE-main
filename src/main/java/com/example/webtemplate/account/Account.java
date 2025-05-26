package com.example.webtemplate.account;

import com.example.webtemplate.account.AccountDataTypes.AccountType;
import com.example.webtemplate.account.AccountDataTypes.RoleType;
import com.example.webtemplate.common.model.SoftDeleteable;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "accounts")
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends SoftDeleteable {

  @Id
  @GeneratedValue
  private Long id;

  @NonNull
  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  @NonNull
  @Setter(AccessLevel.NONE)
  @Enumerated(EnumType.STRING)
  private RoleType role;

  @NonNull
  @Setter(AccessLevel.NONE)
  private String email;

  @NonNull
  @ToString.Exclude
  @Setter(AccessLevel.PACKAGE)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  private String providerUserId;

  // [ ] relationship: `Account` 1 -> 1 `UserProfile`

  @Builder
  private Account(AccountType accountType, RoleType role, String email, String password) {
    this.accountType = accountType;
    this.role = role;
    this.email = email;
    this.password = password;
    this.providerUserId = null;
  }
}