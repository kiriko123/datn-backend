package com.example.demospringsecurity.controller;

import com.example.demospringsecurity.dto.request.user.UserRegisterRequestDTO;
import com.example.demospringsecurity.dto.request.user.UserUpdateRequestDTO;
import com.example.demospringsecurity.dto.response.RestResponse;
import com.example.demospringsecurity.model.User;
import com.example.demospringsecurity.service.UserService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
@Validated
public class UserController {
    private final UserService userService;

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequestDTO updateRequestDTO) {
        log.info("updateUser: {}", updateRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.update(updateRequestDTO));
    }
    @GetMapping
    public ResponseEntity<?> getAllUsers(@Filter Specification<User> specification, Pageable pageable) {
        return ResponseEntity.ok().body(userService.findAll(specification, pageable));
    }
    @DeleteMapping("/{id}")
    public RestResponse<?> deleteUser(@Min(1)@PathVariable Long id) {
        log.info("deleteUser: {}", id);
        userService.delete(id);
        return RestResponse.builder()
                .statusCode(204)
                .message("User deleted")
                .build();
    }
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        log.info("createUser: {}", userRegisterRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRegisterRequestDTO));
    }
    @PostMapping("/bulk-create")
    public RestResponse<?> createUsers(@RequestBody List<UserRegisterRequestDTO> userRegisterRequestDTOS) {
        log.info("createUsers: {}", userRegisterRequestDTOS);
        String res = userService.bulkCreateUser(userRegisterRequestDTOS);
        return RestResponse.builder()
                .statusCode(201)
                .message("Users created")
                .data(res)
                .build();
    }
}
