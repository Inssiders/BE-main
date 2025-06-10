package com.inssider.api.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class SoftDeleteable extends Auditable {

  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  public void delete() {
    this.isDeleted = true;
    this.deletedAt = LocalDateTime.now();
  }

  public void restore() {
    this.isDeleted = false;
    this.deletedAt = null;
  }
}
