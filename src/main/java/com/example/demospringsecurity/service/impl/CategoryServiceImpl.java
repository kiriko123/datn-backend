package com.example.demospringsecurity.service.impl;

import com.example.demospringsecurity.dto.request.category.CategoryCreateRequestDTO;
import com.example.demospringsecurity.dto.request.category.CategoryUpdateRequestDTO;
import com.example.demospringsecurity.dto.response.ResultPaginationResponse;
import com.example.demospringsecurity.dto.response.category.CategoryResponse;
import com.example.demospringsecurity.exception.ResourceNotFoundException;
import com.example.demospringsecurity.mapper.CategoryMapping;
import com.example.demospringsecurity.model.Category;
import com.example.demospringsecurity.repository.CategoryRepository;
import com.example.demospringsecurity.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapping categoryMapping;

    @Override
    public CategoryResponse create(CategoryCreateRequestDTO categoryCreateRequestDTO) {
        Category category = categoryMapping.fromCategoryCreateRequestDTOToCategory(categoryCreateRequestDTO);

        return categoryMapping.fromCategoryToCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(CategoryUpdateRequestDTO categoryUpdateRequestDTO) {
        Category category = categoryMapping.fromCategoryUpdateRequestDTOToCategory(categoryUpdateRequestDTO);
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ResultPaginationResponse getAllCategories(Specification<Category> specification, Pageable pageable) {
        return null;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}
