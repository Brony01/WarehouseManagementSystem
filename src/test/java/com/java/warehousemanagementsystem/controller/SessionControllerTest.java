package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.service.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class SessionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
    }

    @Test
    public void testLogin() throws Exception {
        Map<String, String> sessionData = Map.of("token", "someToken"); // Simulate a login token
        when(sessionService.loginSession("alice", "password123")).thenReturn(sessionData);

        mockMvc.perform(post("/session")
                        .param("username", "alice")
                        .param("password", "password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("someToken"));

        verify(sessionService, times(1)).loginSession("alice", "password123");
    }

    @Test
    public void testLogout() throws Exception {
        when(sessionService.logoutSession("alice")).thenReturn("登出成功！");

        mockMvc.perform(delete("/session")
                        .param("username", "alice")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$.success").value(true));

        verify(sessionService, times(1)).logoutSession("alice");
    }

    @Test
    public void testAdminEndpoint() throws Exception {
        mockMvc.perform(post("/session/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("admin"));
    }

    @Test
    public void testUserEndpoint() throws Exception {
        mockMvc.perform(post("/session/test2"))
                .andExpect(status().isOk())
                .andExpect(content().string("user"));
    }
}
