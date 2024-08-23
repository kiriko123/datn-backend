package com.example.demospringsecurity.service;

import com.example.demospringsecurity.dto.request.user.UserCreateRequestDTO;
import com.example.demospringsecurity.dto.request.user.UserRegisterRequestDTO;
import com.example.demospringsecurity.dto.request.user.UserUpdateRequestDTO;
import com.example.demospringsecurity.dto.response.ResultPaginationResponse;
import com.example.demospringsecurity.dto.response.user.UserResponse;
import com.example.demospringsecurity.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {
    UserResponse save(UserRegisterRequestDTO userRegisterRequestDTO);
    User findByUserName(String username);
    UserResponse update(UserUpdateRequestDTO userUpdateRequestDTO);
    void delete(Long id);
    ResultPaginationResponse findAll(Specification<User> spec, Pageable pageable);
    User findById(Long id);
    User findByEmail(String email);
    void updateUserToken(String token, String email);
    User getUserByEmailAndRefreshToken(String email, String refreshToken);

    long countAllUser();
}
