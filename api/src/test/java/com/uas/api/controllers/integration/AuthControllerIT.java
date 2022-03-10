package com.uas.api.controllers.integration;

import com.google.gson.Gson;
import com.uas.api.repositories.auth.RoleRepository;
import com.uas.api.repositories.auth.UserRepository;
import com.uas.api.response.JwtResponse;
import com.uas.api.security.jwt.AuthEntryPointJwt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
class AuthControllerIT extends BaseIntegrationTest {

    private static final String EMAIL = "maytests@gmail.com";

    private static final String CORRECT_PASSWORD = "maycraftscodes";

    private final Gson GSON = new Gson();

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


//    @Test
    void should_return_jwt_response_when_login_success_given_correct_credentials() throws Exception {
        String loginRequestAsJson = "{\n" +
                "    \"email\": \"" + EMAIL + "\",\n" +
                "    \"password\": \"" + CORRECT_PASSWORD + "\"\n" +
                "}";

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value(EMAIL))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.roles").isArray())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        JwtResponse jwtResponse = GSON.fromJson(responseString, JwtResponse.class);

        mockMvc.perform(get("/api/auth/getUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value(EMAIL))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.firstName").value("May"))
                .andExpect(jsonPath("$.lastName").value("Sanejo"));
    }
}
