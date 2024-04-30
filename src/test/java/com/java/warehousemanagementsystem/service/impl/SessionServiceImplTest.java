package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.mapper.UserMapper;
import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginSessionSuccess() {
        try (MockedStatic<JwtUtils> mockedJwtUtils = Mockito.mockStatic(JwtUtils.class)) {
            String username = "testUser";
            String password = "password";
            User user = new User();
            user.setUsername(username);
            user.setVersion(1);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(mock(Authentication.class));
            when(userMapper.selectOne(any())).thenReturn(user);
            mockedJwtUtils.when(() -> JwtUtils.generateToken(any(String.class), any(Integer.class)))
                    .thenReturn("mockedToken");

            Map<String, String> result = sessionService.loginSession(username, password);

            assertNotNull(result);
            assertEquals("mockedToken", result.get("token"));
            verify(userMapper).selectOne(any());
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }
    }

    @Test
    void loginSessionFailAuthentication() {
        String username = "testUser";
        String password = "password";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        Map<String, String> result = sessionService.loginSession(username, password);

        assertNull(result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userMapper, never()).selectOne(any());
    }

    @Test
    public void logoutSessionSuccess() {
        // Arrange
        UserMapper mockUserMapper = Mockito.mock(UserMapper.class);
        User user = new User();
        user.setVersion(1);  // Assume version starts at 1

        // Setup the mock to return the user when the specific query is run
        Mockito.when(mockUserMapper.selectOne(Mockito.any(QueryWrapper.class)))
                .thenReturn(user);

        SessionServiceImpl service = new SessionServiceImpl();
        service.setUserMapper(mockUserMapper); // Inject mock

        // Act
        String result = service.logoutSession("username");

        // Assert
        assertEquals("登出成功！", result);
        assertEquals(2, user.getVersion()); // Verify that the version is incremented
        Mockito.verify(mockUserMapper).updateById(user); // Verify that updateById is called with the updated user
    }

}
