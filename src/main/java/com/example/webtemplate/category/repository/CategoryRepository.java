package com.example.webtemplate.category.repository;

import com.example.webtemplate.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
