package com.example.webtemplate.category.service;

import com.example.webtemplate.category.entity.Category;
import com.example.webtemplate.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> getCategory(Long id){
        return categoryRepository.findById(id);
    }

}
