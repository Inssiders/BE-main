package com.inssider.api.domains.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inssider.api.common.Util;
import com.inssider.api.common.model.SoftDeleteable;
import com.inssider.api.domains.account.AccountDataTypes.AccountType;
import com.inssider.api.domains.account.AccountDataTypes.RoleType;
import com.inssider.api.domains.auth.token.refresh.RefreshToken;
import com.inssider.api.domains.profile.UserProfile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends SoftDeleteable {

  @Id @GeneratedValue private Long id;

  @OneToOne(mappedBy = "account", cascade = CascadeType.PERSIST)
  private UserProfile profile;

  @OneToOne(mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private RefreshToken refreshToken;

  @NonNull
  @ToString.Exclude
  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  @NonNull
  @Enumerated(EnumType.STRING)
  private RoleType role;

  @NonNull private String email;

  @NonNull
  @ToString.Exclude
  @Setter(AccessLevel.PACKAGE)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @ToString.Exclude private String providerUserId;

  @Builder
  private Account(
      @NonNull AccountType accountType,
      @NonNull RoleType role,
      @NonNull String email,
      @NonNull String password) {
    this.accountType = accountType;
    this.role = role;
    this.email = email;
    this.password = password;

    this.profile = UserProfile.builder().account(this).nickname(email).build();
  }

  @PrePersist
  @PreUpdate
  private void hashPassword() {
    this.password = Util.argon2Hash(this.password);
  }

  @Override
  public void delete() {
    super.delete();
    this.profile = null;
    this.refreshToken = null;
  }

  @Override
  public void restore() {
    super.restore();
    this.profile =
        UserProfile.builder()
            .account(this)
            .nickname(email)
            .accountVisible(true)
            .followerVisible(true)
            .build();
  }
}
