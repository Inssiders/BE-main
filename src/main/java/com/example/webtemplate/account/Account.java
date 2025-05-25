package com.example.webtemplate.account;

import com.example.webtemplate.account.model.AccountType;
import com.example.webtemplate.account.model.RoleType;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
class Account {

  private @Id @GeneratedValue Long id;

  @NonNull
  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  @NonNull
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

  @NonNull
  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  private String providerUserId;
}
