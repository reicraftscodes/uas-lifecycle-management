package com.uas.api.services;

import com.uas.api.models.auth.User;
import com.uas.api.repositories.auth.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @MockBean
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void fetchValidUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        boolean result = userService.userExistsById(1);
        assertTrue("User exists, result should be true", result);
    }

    @Test
    public void fetchInvalidUser() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        boolean result = userService.userExistsById(1);
        assertFalse("User doesn't exist, result should be false", result);
    }
}
