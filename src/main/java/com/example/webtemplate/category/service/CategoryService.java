package com.example.webtemplate.category.service;

import com.example.webtemplate.category.CategoryType;
import com.example.webtemplate.category.entity.Category;
import com.example.webtemplate.category.repository.CategoryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public Optional<Category> getCategory(CategoryType categorytype) {
    return categoryRepository.findByType(categorytype);
  }
}
