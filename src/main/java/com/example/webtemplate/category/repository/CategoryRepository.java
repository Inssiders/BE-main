package com.example.webtemplate.category.repository;

import com.example.webtemplate.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    Optional<Category> findById(Long categoryId);

    Optional<Category> findByName(String name);
}
