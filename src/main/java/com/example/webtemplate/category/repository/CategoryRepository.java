package com.example.webtemplate.category.repository;

import com.example.webtemplate.category.CategoryType;
import com.example.webtemplate.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByType(CategoryType type);
}
