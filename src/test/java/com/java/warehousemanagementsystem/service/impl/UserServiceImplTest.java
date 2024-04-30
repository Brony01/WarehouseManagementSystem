package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.mapper.UserMapper;
import com.java.warehousemanagementsystem.pojo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserSuccessfully() {
        String username = "testUser";
        String password = "password";
        String confirmedPassword = "password";

        when(userMapper.selectCount(any())).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userMapper.insert(any(User.class))).thenReturn(1);

        assertTrue(userService.register(username, password, confirmedPassword));
    }

    @Test
    void registerUserFailDueToExistingUser() {
        String username = "existingUser";
        String password = "password";
        String confirmedPassword = "password";

        when(userMapper.selectCount(any())).thenReturn(1L);

        assertThrows(IllegalArgumentException.class, () -> userService.register(username, password, confirmedPassword));
    }

    @Test
    void updateUserSuccessfully() {
        String username = "existingUser";
        String password = "newPassword";
        String confirmedPassword = "newPassword";
        User existingUser = new User(1, username, "oldPassword", 1);

        when(userMapper.selectOne(any())).thenReturn(existingUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        assertTrue(userService.updateUser(username, password, confirmedPassword));
    }

    @Test
    void updateUserFailDueToNonexistentUser() {
        String username = "nonexistentUser";
        String password = "password";
        String confirmedPassword = "password";

        when(userMapper.selectOne(any())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(username, password, confirmedPassword));
    }

    @Test
    void deleteUserSuccessfully() {
        Integer userId = 1;

        when(userMapper.deleteById(userId)).thenReturn(1);

        assertTrue(userService.deleteUser(userId));
    }

    @Test
    void deleteUserFailDueToInvalidId() {
        Integer userId = null;

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void findUserByIdSuccessfully() {
        Integer userId = 1;
        User foundUser = new User(userId, "userName", "encodedPassword", 1);

        when(userMapper.selectById(userId)).thenReturn(foundUser);

        User result = userService.findUserById(userId);
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertNull(result.getPassword()); // Ensuring password is null for safety
    }

    @Test
    void findUserByIdFailDueToNullId() {
        assertThrows(IllegalArgumentException.class, () -> userService.findUserById(null));
    }
}
