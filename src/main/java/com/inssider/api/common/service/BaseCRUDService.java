package com.inssider.api.common.service;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository 기반 서비스 인터페이스 default 메서드로 기본 구현 제공 */
public interface BaseCRUDService<T, ID, R extends JpaRepository<T, ID>> {

  R getRepository();

  default T save(T entity) {
    return getRepository().save(entity);
  }

  default Optional<T> findById(ID id) {
    return getRepository().findById(id);
  }

  default boolean existsById(ID id) {
    return getRepository().existsById(id);
  }

  default void deleteById(ID id) {
    getRepository().deleteById(id);
  }

  default long count() {
    return getRepository().count();
  }
}
