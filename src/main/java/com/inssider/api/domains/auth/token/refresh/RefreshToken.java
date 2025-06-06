package com.inssider.api.domains.auth.token.refresh;

import com.inssider.api.domains.account.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "refresh_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class RefreshToken {

  @Id private Long id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @NonNull private String token;
}
