package com.uas.api.controllers.integration;

import com.google.gson.Gson;
import com.uas.api.models.auth.ERole;
import com.uas.api.models.auth.Role;
import com.uas.api.models.auth.User;
import com.uas.api.repositories.auth.RoleRepository;
import com.uas.api.repositories.auth.UserRepository;
import com.uas.api.response.JwtResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public abstract class BaseIntegrationTest {
    private static final String EMAIL = "maytests@gmail.com";
    private static final String CORRECT_PASSWORD = "maycraftscodes";
    private static final String INCORRECT_PASSWORD = "incorrectpasswords";
    @Autowired
    protected MockMvc mockMvc;

    protected Gson gson = new Gson();

    protected User user;

    protected String token;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected abstract void afterEach();

    protected abstract void beforeEach();

    @AfterEach
    void tearDown() {
        afterEach();
    }

    @BeforeEach
    void setup() throws Exception {


        user = createUserAccount();

        String loginRequestAsJson = "{\n" +
                "    \"email\": \"" + EMAIL + "\",\n" +
                "    \"password\": \"" + CORRECT_PASSWORD + "\"\n" +
                "}";
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestAsJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JwtResponse jwtResponse = gson.fromJson(response, JwtResponse.class);
        token = jwtResponse.getToken();
    }

    private User createUserAccount() {
        Role role = new Role();
        role.setName(ERole.ROLE_USER_LOGISTIC);
        role.setName(ERole.ROLE_USER);
        role.setName(ERole.ROLE_USER_CTO);
        role.setName(ERole.ROLE_USER_CEO);
        role.setName(ERole.ROLE_USER_COO);

        Set<Role> roles = new HashSet<>();
        Role userLogisticOfficerRoleDb = roleRepository.
                findRoleByRoleName(ERole.ROLE_USER_LOGISTIC)
                .orElseThrow(() -> new RuntimeException("Error: Logistic Role is not found."));
        roles.add(userLogisticOfficerRoleDb);

        Role userRoleDb = roleRepository.
                findRoleByRoleName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: User Role is not found."));
        roles.add(userRoleDb);

        Role ctoRoleDb = roleRepository.
                findRoleByRoleName(ERole.ROLE_USER_LOGISTIC)
                .orElseThrow(() -> new RuntimeException("Error: Cto Role is not found."));
        roles.add(ctoRoleDb);

        Role ceoRoleDb = roleRepository.
                findRoleByRoleName(ERole.ROLE_USER_LOGISTIC)
                .orElseThrow(() -> new RuntimeException("Error: Ceo Role is not found."));
        roles.add(ceoRoleDb);

        Role cooRoleDb = roleRepository.
                findRoleByRoleName(ERole.ROLE_USER_LOGISTIC)
                .orElseThrow(() -> new RuntimeException("Error: Coo Role is not found."));
        roles.add(cooRoleDb);


        User user = new User(EMAIL, EMAIL,
                passwordEncoder.encode(CORRECT_PASSWORD),
                "May",
                "Sanejo",
                roles,
                "fdacad9e-b98c-4b16-bede-280e1e6a446c");
        return userRepository.save(user);
    }
}



