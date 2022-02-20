package com.uas.api.controllers.integration;

import com.uas.api.controllers.BaseIntegrationTest;
import com.uas.api.repositories.auth.RoleRepository;
import com.uas.api.repositories.auth.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class LoginControllerTest extends BaseIntegrationTest {

    private static final String EMAIL = "maycraftscodes@gmail.com";
    private static final String CORRECT_PASSWORD = "maycraftscodes";
    private static final String INCORRECT_PASSWORD = "incorrectpassword";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Override
    protected void afterEach() {
    }

    @Override
    protected void beforeEach() {

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void should_return_jwt_response_when_login_success_given_correct_credentials() throws Exception {
        String loginRequestAsJson = "{\n" +
                "    \"email\": \"" + EMAIL + "\",\n" +
                "    \"password\": \"" + CORRECT_PASSWORD + "\"\n" +
                "}";

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value(EMAIL))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.roles").isArray());
    }

    @Test
    void should_return_error_when_login_failed_given_incorrect_credentials() throws Exception {
        String loginRequestAsJson = "{\n" +
                "    \"email\": \"" + EMAIL + "\",\n" +
                "    \"password\": \"" + INCORRECT_PASSWORD + "\"\n" +
                "}";

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid email or password!"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

}
