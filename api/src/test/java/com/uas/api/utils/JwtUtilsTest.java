package com.uas.api.utils;

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
    void should_return_jwtSecret_when_getJwtSecret_given_uas2021() {
        //given
        String expectedJwtSecret = "uas2021";

        //when
        String jwtSecret = jwtUtils.getJwtSecret();

        //then
        assertEquals(expectedJwtSecret, jwtSecret);
    }

    @Test
    void should_return_jwtExpirationMs_when_getJwtExpirationMs_given_86400000() {

        //given
        int expectedExpirationMs = 864000000;

        //when
        int jwtExpirationMs = jwtUtils.getJwtExpirationMs();

        //then
        assertEquals(expectedExpirationMs, jwtExpirationMs);
    }

}
