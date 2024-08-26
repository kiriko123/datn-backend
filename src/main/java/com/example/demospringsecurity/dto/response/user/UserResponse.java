package com.example.demospringsecurity.dto.response.user;

import com.example.demospringsecurity.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Builder
@Setter
public class UserResponse implements Serializable {

    private long id;

    private String name;

    private String email;

    private String address;

    private int age;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;
    private Role role;

    public static UserResponse fromUserToUserResponse(User user) {

        UserResponse userResponse =  UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();



        if (user.getRole() != null) {
            userResponse.setRole(Role.builder()
                    .id(user.getRole().getId())
                    .name(user.getRole().getName())
                    .build());
        }else{
            userResponse.setRole(null);
        }
        return userResponse;

    }
    @Getter
    @Setter
    @Builder
    public static class Role{
        private long id;
        private String name;
    }
}
