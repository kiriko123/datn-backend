package com.example.demospringsecurity.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
public class CategoryCreateRequestDTO {
    @NotBlank
    String name;
    String imageUrl;
}
