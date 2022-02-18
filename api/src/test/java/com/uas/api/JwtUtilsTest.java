package com.uas.api;

import com.uas.api.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void should_return_jwtSecret_when_getJwtSecret_given_woAIni2020S() {
        //given
        String expectedJwtSecret = "uasLifecycleManagementSystemSecretKey";

        //when
        String jwtSecret = jwtUtils.getJwtSecret();

        //then
        assertEquals(expectedJwtSecret, jwtSecret);
    }

    @Test
    void should_return_jwtExpirationMs_when_getJwtExpirationMs_given_86400000() {

        //given
        int expectedjwtExpirationMs = 86400000;

        //when
        int jwtExpirationMs = jwtUtils.getJwtExpirationMs();

        //then
        assertEquals(expectedjwtExpirationMs, jwtExpirationMs);
    }

}
