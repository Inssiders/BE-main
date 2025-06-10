package com.inssider.api.domains.like.entity;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.like.LikeTargetType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

  @Id @GeneratedValue private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LikeTargetType targetType;

  private Long targetId;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;
}
