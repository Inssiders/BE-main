package com.inssider.api.domains.auth.token;

import com.inssider.api.domains.account.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class RefreshToken {

  @Id private Long id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "account_id", nullable = false)
  @NonNull
  private Account account;

  @NonNull
  @Getter
  @Setter
  @Column(length = 1000)
  private String token;

  @PrePersist
  private void prePersist() {
    if (account != null && account.getRefreshToken() == null) {
      account.setRefreshToken(this);
    }
  }

  @PreRemove
  private void preRemove() {
    if (account != null) {
      account.setRefreshToken(null);
    }
  }
}
