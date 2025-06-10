package com.inssider.api.domains.tag.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

  @Id @GeneratedValue private Long id;

  @Column(nullable = false, length = 255)
  private String name;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime created_at;
}
