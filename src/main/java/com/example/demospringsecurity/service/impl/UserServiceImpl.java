package com.example.demospringsecurity.service.impl;

import com.example.demospringsecurity.dto.request.user.UserCreateRequestDTO;
import com.example.demospringsecurity.dto.request.user.UserRegisterRequestDTO;
import com.example.demospringsecurity.dto.request.user.UserUpdateRequestDTO;
import com.example.demospringsecurity.dto.response.ResultPaginationResponse;
import com.example.demospringsecurity.dto.response.user.UserResponse;
import com.example.demospringsecurity.service.UserService;
import com.example.demospringsecurity.exception.InvalidDataException;
import com.example.demospringsecurity.exception.ResourceNotFoundException;

import com.example.demospringsecurity.model.Role;
import com.example.demospringsecurity.model.User;

import com.example.demospringsecurity.repository.RoleRepository;
import com.example.demospringsecurity.repository.UserRepository;
import com.example.demospringsecurity.util.EmailValidator;
import com.example.demospringsecurity.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse save(UserRegisterRequestDTO userRegisterRequestDTO) {

        if (userRepository.existsByEmail(userRegisterRequestDTO.getEmail())) {
            throw new InvalidDataException("Email already exists");
        }

        User user = userRepository.save(
                User.builder()
                        .name(userRegisterRequestDTO.getName())
                        .password(passwordEncoder.encode(userRegisterRequestDTO.getPassword()))
                        .email(userRegisterRequestDTO.getEmail())
                        .role(roleRepository.findByName("ROLE_USER"))
                        .enabled(true)
                        .build());

        return UserResponse.fromUserToUserResponse(user);
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByName(username);
    }

    @Override
    public UserResponse update(UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = this.findById(userUpdateRequestDTO.getId());
        user.setName(userUpdateRequestDTO.getName());
        user.setPassword(passwordEncoder.encode(userUpdateRequestDTO.getPassword()));
        return UserResponse.fromUserToUserResponse(userRepository.save(user));
    }


    @Override
    public void delete(Long id) {

        User user = findById(id);
        String email = SecurityUtil.getCurrentUserLogin().orElse("");


        if (user.getEmail().equals(email)) {
            throw new RuntimeException("Không thể xóa user hiện tại của bạn");
        }
        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Không thể xóa ADMIN");
        }
        userRepository.deleteById(id);
    }

    @Override
    public ResultPaginationResponse findAll(Specification<User> spec, Pageable pageable) {

        Page<User> users = userRepository.findAll(spec, pageable);

        ResultPaginationResponse.Meta meta = ResultPaginationResponse.Meta.builder()
                .total(users.getTotalElements())
                .pages(users.getTotalPages())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .build();

        List<UserResponse> userResponses = users.getContent().stream().map(UserResponse::fromUserToUserResponse).toList();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(userResponses)
                .build();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUserToken(String token, String email) {
        User user = findByEmail(email);
        user.setRefreshToken(token);
        userRepository.save(user);
    }

    @Override
    public User getUserByEmailAndRefreshToken(String email, String refreshToken) {
        return userRepository.findByEmailAndRefreshToken(email, refreshToken);
    }

    @Override
    public String bulkCreateUser(List<UserRegisterRequestDTO> userRegisterRequestDTOS) {
        int error = 0;
        int success = 0;
        for (UserRegisterRequestDTO userRegisterRequestDTO : userRegisterRequestDTOS) {
            if(userRepository.existsByEmail(userRegisterRequestDTO.getEmail())) {
                error++;
                continue;
            }
            if(!EmailValidator.isValidEmail(userRegisterRequestDTO.getEmail())){
                error++;
                continue;
            }
            if(userRegisterRequestDTO.getName().isBlank() || userRegisterRequestDTO.getPassword().isBlank()){
                error++;
                continue;
            }
            User user = userRepository.save(
                    User.builder()
                            .name(userRegisterRequestDTO.getName())
                            .password(passwordEncoder.encode(userRegisterRequestDTO.getPassword()))
                            .email(userRegisterRequestDTO.getEmail())
                            .role(roleRepository.findByName("ROLE_USER"))
                            .enabled(true)
                            .build()
            );
            success++;
        }

        return "Success: " + success + " Error: " + error;
    }

    @Override
    public long countAllUser() {
        return userRepository.count();
    }
}