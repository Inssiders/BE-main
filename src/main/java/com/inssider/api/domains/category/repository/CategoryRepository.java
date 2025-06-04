package com.inssider.api.domains.category.repository;

import com.inssider.api.domains.category.CategoryType;
import com.inssider.api.domains.category.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByType(CategoryType type);
}
