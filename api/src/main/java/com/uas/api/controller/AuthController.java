package com.uas.api.controller;


import com.uas.api.mapper.UserMapper;
import com.uas.api.models.auth.User;
import com.uas.api.models.dtos.*;
import com.uas.api.response.JwtResponse;
import com.uas.api.requests.LoginRequest;
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

    /**
     * Authentication service.
     */
    @Autowired
    private AuthService authService;

    /**
     * UserDImpl will Autowire and use our AddressMapper.
     */

    @Autowired
    private UserMapper userMapper;

    /**
     * A post mapping that allows a user to login .
     * @param loginRequest request body.
     * @return response entity with response.
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    /**
     * A post mapping that allows a user to create an account .
     * @param signUpRequest request body.
     * @return response entity with response.
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody final SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    /**
     * A post mapping that allows a user to view jwt info.
     * @return getJwtReponse in return.
     */
    @PostMapping("/getJwtInfo")
    @PreAuthorize("hasRole('USER') or hasRole('ROLE_USER_CTO') or hasRole('ROLE_USER_LOGISTIC') or hasRole('ROLE_USER_CEO') or hasRole('ROLE_USER_COO') ")
    public ResponseEntity<JwtResponse> getJwtTokenInfo() {
        return authService.getJwtResponse();
    }

    /**
     * A post mapping that allows a user to view user information.
     * @return userMap to userDTO.
     */
    @GetMapping("/getUserInfo")
    @PreAuthorize("hasRole('USER') or hasRole('ROLE_USER_CTO') or hasRole('ROLE_USER_LOGISTIC') or hasRole('ROLE_USER_CEO') or hasRole('ROLE_USER_COO') ")
    public UserDTO getUserInfo() {
        User user = authService.getUserInfo();
        return userMapper.toUserDto(user);
    }
}

