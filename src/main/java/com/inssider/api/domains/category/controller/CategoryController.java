package com.inssider.api.domains.category.controller;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.domains.category.CategoryType;
import com.inssider.api.domains.category.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/categories")
  public ResponseEntity<BaseResponse.ResponseWrapper<List<CategoryType>>> getCategories() {
    List<CategoryType> data = categoryService.get();
    return BaseResponse.of(200, data);
  }
}
