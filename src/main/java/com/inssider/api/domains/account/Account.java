package com.inssider.api.domains.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inssider.api.common.model.SoftDeleteable;
import com.inssider.api.domains.account.AccountDataTypes.AccountType;
import com.inssider.api.domains.account.AccountDataTypes.RoleType;
import com.inssider.api.domains.auth.token.RefreshToken;
import com.inssider.api.domains.profile.UserProfile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends SoftDeleteable {

  @Id @GeneratedValue private Long id;

  @OneToOne(
      mappedBy = "account",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private UserProfile profile;

  @OneToOne(mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @Setter
  private RefreshToken refreshToken;

  @Column(nullable = false)
  @ToString.Exclude
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private final AccountType accountType = AccountType.PASSWORD;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private final RoleType role = RoleType.USER;

  @Column(unique = true, nullable = false)
  // [ ] index
  private String email;

  @Column(nullable = false)
  @ToString.Exclude
  @Setter(AccessLevel.PACKAGE)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @ToString.Exclude private String providerUserId;

  @PrePersist
  public void prePersist() {
    this.profile = UserProfile.builder().account(this).nickname(email).build();
  }

  @Override
  public void postSoftDelete() {
    this.profile.softDelete(); // cascade soft delete to profile
  }

  @Override
  public void postRestore() {
    this.profile.restore(); // cascade restore to profile
  }
}
