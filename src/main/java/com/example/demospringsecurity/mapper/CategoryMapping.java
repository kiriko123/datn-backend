package com.example.demospringsecurity.mapper;

import com.example.demospringsecurity.dto.request.category.CategoryCreateRequestDTO;
import com.example.demospringsecurity.dto.request.category.CategoryUpdateRequestDTO;
import com.example.demospringsecurity.dto.response.category.CategoryResponse;
import com.example.demospringsecurity.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapping {
    Category fromCategoryCreateRequestDTOToCategory(CategoryCreateRequestDTO categoryCreateRequestDTO);
    Category fromCategoryUpdateRequestDTOToCategory(CategoryUpdateRequestDTO categoryUpdateRequestDTO);
    CategoryResponse fromCategoryToCategoryResponse(Category category);
}
