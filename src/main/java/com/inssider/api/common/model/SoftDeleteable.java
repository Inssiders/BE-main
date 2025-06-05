package com.inssider.api.common.model;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class SoftDeleteable extends Auditable {

  private boolean isDeleted = false;
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
