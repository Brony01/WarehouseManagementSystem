package com.java.warehousemanagementsystem.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegister() throws Exception {
        when(userService.register("alice", "password123", "password123")).thenReturn(true);

        mockMvc.perform(post("/user")
                        .param("username", "alice")
                        .param("password", "password123")
                        .param("confirmedPassword", "password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(userService, times(1)).register("alice", "password123", "password123");
    }

    @Test
    public void testUpdate() throws Exception {
        when(userService.updateUser(1, "alice", "newpassword", "newpassword")).thenReturn(true);

        mockMvc.perform(put("/user")
                        .param("id", "1")
                        .param("username", "alice")
                        .param("password", "newpassword")
                        .param("confirmedPassword", "newpassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(userService, times(1)).updateUser(1, "alice", "newpassword", "newpassword");
    }

    @Test
    public void testFindUserById() throws Exception {
        User user = new User(); // Set user properties if needed
        when(userService.findUserById(1)).thenReturn(user);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(userService, times(1)).findUserById(1);
    }

    @Test
    public void testGetList() throws Exception {
        List<User> users = Arrays.asList(new User(), new User()); // Create test users
        when(userService.findAllUser()).thenReturn(users);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(users.size()));

        verify(userService, times(1)).findAllUser();
    }

    @Test
    public void testDelete() throws Exception {
        when(userService.deleteUser(1)).thenReturn(true);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(userService, times(1)).deleteUser(1);
    }
}
