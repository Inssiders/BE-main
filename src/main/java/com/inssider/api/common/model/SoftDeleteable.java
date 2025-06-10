package com.inssider.api.common.model;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class SoftDeleteable extends Auditable {

  private boolean isDeleted = false;
  private LocalDateTime deletedAt;

  public void softDelete() {
    preSoftDelete();
    this.isDeleted = true;
    this.deletedAt = LocalDateTime.now();
    postSoftDelete();
  }

  public void restore() {
    preRestore();
    this.isDeleted = false;
    this.deletedAt = null;
    postRestore();
  }

  /**
   * Soft delete 실행 전에 호출되는 hook 메서드입니다.
   *
   * <p>서브클래스에서 필요한 전처리 로직을 구현하세요.
   *
   * @see #softDelete()
   * @see #postSoftDelete()
   */
  protected void preSoftDelete() {}

  /**
   * Soft delete 실행 후에 호출되는 hook 메서드입니다.
   *
   * <p>서브클래스에서 필요한 후처리 로직을 구현하세요.
   *
   * @see #softDelete()
   * @see #preSoftDelete()
   */
  protected void postSoftDelete() {}

  /**
   * Restore 실행 전에 호출되는 hook 메서드입니다.
   *
   * <p>서브클래스에서 필요한 전처리 로직을 구현하세요.
   *
   * @see #restore()
   * @see #postRestore()
   */
  protected void preRestore() {}

  /**
   * Restore 실행 후에 호출되는 hook 메서드입니다.
   *
   * <p>서브클래스에서 필요한 후처리 로직을 구현하세요.
   *
   * @see #restore()
   * @see #preRestore()
   */
  protected void postRestore() {}
}
