package com.example.demospringsecurity.controller;

import com.example.demospringsecurity.dto.request.category.CategoryCreateRequestDTO;
import com.example.demospringsecurity.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/category")
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping
    public ResponseEntity<?> create(@Valid CategoryCreateRequestDTO categoryCreateRequestDTO) {
        log.info("Create category: {}", categoryCreateRequestDTO);
        return ResponseEntity.ok(categoryService.create(categoryCreateRequestDTO));
    }
}
