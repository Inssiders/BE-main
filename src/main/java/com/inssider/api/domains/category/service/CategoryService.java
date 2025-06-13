package com.inssider.api.domains.category.service;

import com.inssider.api.domains.category.CategoryType;
import com.inssider.api.domains.category.entity.Category;
import com.inssider.api.domains.category.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public Optional<Category> getCategory(CategoryType categorytype) {
    return categoryRepository.findByType(categorytype);
  }

  public List<CategoryType> get() {
    return categoryRepository.findAll().stream()
        .map(Category::getType)
        .collect(Collectors.toList());
  }
}
