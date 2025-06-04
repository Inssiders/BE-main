package com.inssider.api.domains.auth.code.email;

import com.inssider.api.common.Util;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "email_verification_codes")
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class EmailAuthCode {

  @Id private String email;

  @Column(nullable = false, updatable = false)
  private String code;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "expired_at")
  private LocalDateTime expiredAt;

  public EmailAuthCode(String email) {
    this.email = email;
    this.code = Util.codeGenerator().get();
  }

  @PostPersist
  private void setExpiredAt() {
    this.expiredAt = this.createdAt.plusMinutes(5);
  }
}
