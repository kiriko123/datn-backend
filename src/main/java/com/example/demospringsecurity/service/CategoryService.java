package com.example.demospringsecurity.service;

import com.example.demospringsecurity.dto.request.category.CategoryCreateRequestDTO;
import com.example.demospringsecurity.dto.request.category.CategoryUpdateRequestDTO;
import com.example.demospringsecurity.dto.response.ResultPaginationResponse;
import com.example.demospringsecurity.dto.response.category.CategoryResponse;
import com.example.demospringsecurity.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CategoryService {
    CategoryResponse create(CategoryCreateRequestDTO categoryCreateRequestDTO);
    CategoryResponse update(CategoryUpdateRequestDTO categoryUpdateRequestDTO);
    void delete(Long id);
    ResultPaginationResponse getAllCategories(Specification<Category> specification, Pageable pageable);
    Category getCategoryById(Long id);
}
