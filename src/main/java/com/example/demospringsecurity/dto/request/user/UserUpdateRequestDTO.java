package com.example.demospringsecurity.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
public class UserUpdateRequestDTO {
    @NotNull
    private long id;
    @NotBlank
    @Size(min = 6, max = 20)
    private String name;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
