package com.uas.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uas.api.controller.AuthController;
import com.uas.api.controller.PartsController;
import com.uas.api.controllers.integration.BaseIntegrationTest;
import com.uas.api.exceptions.EmailAlreadyExistException;
import com.uas.api.exceptions.EmailConfirmException;
import com.uas.api.exceptions.PasswordConfirmException;
import com.uas.api.exceptions.UserNotFoundException;
import com.uas.api.mapper.UserMapper;
import com.uas.api.repositories.auth.RoleRepository;
import com.uas.api.repositories.auth.UserRepository;
import com.uas.api.requests.LoginRequest;
import com.uas.api.requests.SignupRequest;
import com.uas.api.response.MessageResponse;
import com.uas.api.security.jwt.AuthEntryPointJwt;
import com.uas.api.security.jwt.AuthTokenFilter;
import com.uas.api.security.jwt.JwtUtils;
import com.uas.api.services.auth.AuthService;
import com.uas.api.services.auth.UserDetailsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SignupControllerIT  {

    private static final String EMAIL = "maytests@gmail.com";
    private static final String EMAIL2 = "nonexistentuser@gmail.com";
    private static final String CORRECT_PASSWORD = "maycraftscodes";
    private static final String INCORRECT_CONFIRM_PASSWORD = "incorrectpassword";

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private AuthService authService;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;
    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void whenValidUserLogsInShouldReturn200Response() throws Exception {
        LoginRequest mockLogin = new LoginRequest(EMAIL2, CORRECT_PASSWORD);
        String json = objectMapper.writeValueAsString(mockLogin);
        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }
    @Test
    public void whenEmptyPasswordLogsInShouldReturnBadCredentailsException() throws Exception {
        LoginRequest mockLogin = new LoginRequest(EMAIL2, "");
        String json = objectMapper.writeValueAsString(mockLogin);
        when(authService.authenticateUser(mockLogin)).thenThrow(new BadCredentialsException("Invalid Password!"));
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid email or password!"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }
    @Test
    public void whenEmptyEmailLogsInShouldReturnBadCredentailsException() throws Exception {
        LoginRequest mockLogin = new LoginRequest("", CORRECT_PASSWORD);
        String json = objectMapper.writeValueAsString(mockLogin);
        when(authService.authenticateUser(mockLogin)).thenThrow(new BadCredentialsException("Invalid Email!"));
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid email or password!"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }
    @Test
    public void whenValidLoginRequestIsMadeButUserNotFoundThenShouldThrowUserNotFoundException() throws Exception {
        LoginRequest mockLogin = new LoginRequest(EMAIL2, CORRECT_PASSWORD);
        String json = objectMapper.writeValueAsString(mockLogin);
        when(authService.authenticateUser(mockLogin)).thenThrow(new UserNotFoundException("User not found!"));
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User not found!"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @Test
    void should_return_success_when_signup_given_new_valid_user() throws Exception {
        SignupRequest mockSignUpRequest = new SignupRequest(EMAIL2, EMAIL2, CORRECT_PASSWORD, CORRECT_PASSWORD, "Mock", "User");
        String json = objectMapper.writeValueAsString(mockSignUpRequest);
        when(authService.registerUser(mockSignUpRequest)).thenReturn(new MessageResponse("User registered successfully!"));
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    void should_return_error_when_signup_failed_given_email_already_exist() throws Exception {
        SignupRequest mockSignUpRequest = new SignupRequest(EMAIL2, EMAIL2, CORRECT_PASSWORD, CORRECT_PASSWORD, "Mock", "User");
        String json = objectMapper.writeValueAsString(mockSignUpRequest);
        when(authService.registerUser(mockSignUpRequest)).thenThrow(new EmailAlreadyExistException("Email already exist! Please use another email."));
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already exist! Please use another email."))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }
    @Test
    public void whenEmailsDoNotMatchShouldReturnEmailConfirmException() throws Exception {
        SignupRequest mockSignUpRequest = new SignupRequest(EMAIL2, EMAIL2, CORRECT_PASSWORD, CORRECT_PASSWORD, "Mock", "User");
        String json = objectMapper.writeValueAsString(mockSignUpRequest);
        when(authService.registerUser(mockSignUpRequest)).thenThrow(new EmailConfirmException("Please retype your email."));
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Please retype your email."))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }
    @Test
    void should_return_error_when_signup_failed_given_incorrect_confirm_password() throws Exception {
        SignupRequest mockSignUpRequest = new SignupRequest(EMAIL2, EMAIL2, CORRECT_PASSWORD, INCORRECT_CONFIRM_PASSWORD, "Mock", "User");
        String json = objectMapper.writeValueAsString(mockSignUpRequest);
        when(authService.registerUser(mockSignUpRequest)).thenThrow(new PasswordConfirmException("Please retype your password!"));
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Please retype your password!"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void whenUserRequestsJwtInfoShouldReturn200OK() throws Exception {
        mockMvc.perform(get("/api/auth/getJwtInfo"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"USER"})
    public void whenUserRequestsUserInfoShouldReturn200OK() throws Exception {
        mockMvc.perform(get("/api/auth/getUserInfo"))
                .andExpect(status().isOk());
    }


}

