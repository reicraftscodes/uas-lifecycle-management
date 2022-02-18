package com.uas.api.controller;


import com.uas.api.mapper.UserMapper;
import com.uas.api.models.auth.User;
import com.uas.api.models.dtos.*;
import com.uas.api.response.JwtResponse;
import com.uas.api.response.LoginRequest;
import com.uas.api.response.MessageResponse;
import com.uas.api.requests.SignupRequest;
import com.uas.api.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @PostMapping("/getJwtInfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JwtResponse> getJwtTokenInfo() {
        return authService.getJwtResponse();
    }

    @GetMapping("/getUserInfo")
    @PreAuthorize("hasRole('USER')")
    public UserDTO getUserInfo() {
        User user = authService.getUserInfo();
        return userMapper.toUserDto(user);
    }
}