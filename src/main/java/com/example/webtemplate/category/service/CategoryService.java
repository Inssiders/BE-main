package com.example.webtemplate.category.service;

import com.example.webtemplate.category.CategoryType;
import com.example.webtemplate.category.entity.Category;
import com.example.webtemplate.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> getCategory(CategoryType categorytype) {
        return categoryRepository.findByType(categorytype);
    }

}
