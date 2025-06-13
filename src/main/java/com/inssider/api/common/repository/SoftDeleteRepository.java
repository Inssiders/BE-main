package com.inssider.api.common.repository;

import com.inssider.api.common.model.SoftDeleteable;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface SoftDeleteRepository<T extends SoftDeleteable, ID> extends JpaRepository<T, ID> {

  @Override
  @Query("SELECT e FROM #{#entityName} e WHERE e.isDeleted = false")
  @NonNull
  List<T> findAll();

  @Override
  @Query("SELECT e FROM #{#entityName} e WHERE e.id = ?1 AND e.isDeleted = false")
  @NonNull
  Optional<T> findById(@NonNull ID id);

  @Override
  @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.isDeleted = false")
  long count();

  @Transactional
  @Modifying
  @Query(
      "UPDATE #{#entityName} e SET e.isDeleted = true, e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = ?1")
  void softDelete(ID id);

  @Query("SELECT e FROM #{#entityName} e")
  List<T> findAllIncludeDeleted();

  @Query("SELECT e FROM #{#entityName} e WHERE e.isDeleted = true")
  List<T> findAllDeleted();
}
