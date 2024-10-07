package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.dto.common.DataResponse;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.dto.user.UserCreationRequest;
import com.bfs.hibernateprojectdemo.domain.dto.user.UserLoginRequest;
import com.bfs.hibernateprojectdemo.exception.InvalidCredentialsException;
import com.bfs.hibernateprojectdemo.out.LoginAdminUserOut;
import com.bfs.hibernateprojectdemo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public DataResponse registerUser(@Valid @RequestBody UserCreationRequest request, BindingResult result) {
        if (result.hasErrors()) return DataResponse.builder()
                .success(false)
                .message("Validation errors")
                .build();

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        try {
            User registeredUser = userService.registerUser(user);
            return DataResponse.builder()
                    .success(true)
                    .message("User registered successfully")
                    .data(registeredUser)
                    .build();
        } catch (RuntimeException e) {
            return DataResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest loginRequest) {
        try {
            LoginAdminUserOut user = userService.loginUser(loginRequest);
            return ResponseEntity.ok(DataResponse.<User>builder()
                    .success(true)
                    .message("Login successful")
                    .data(user)
                    .build());
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.badRequest().body(DataResponse.<Object>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/{id}")
    public DataResponse getUserById(@PathVariable int id) {
        return DataResponse.builder()
                .success(true)
                .message("Success")
                .data(userService.getUserById(id))
                .build();
    }

    @PostMapping
    public DataResponse addUser(@Valid @RequestBody UserCreationRequest request, BindingResult result) {
        if (result.hasErrors()) return DataResponse.builder()
                                            .success(false)
                                            .message("Something went wrong")
                                            .build();

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();

        userService.addUser(user);

        return DataResponse.builder()
                .success(true)
                .message("Success")
                .build();
    }
}