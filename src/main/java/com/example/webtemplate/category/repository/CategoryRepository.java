package com.example.webtemplate.category.repository;

import com.example.webtemplate.category.CategoryType;
import com.example.webtemplate.category.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByType(CategoryType type);
}
