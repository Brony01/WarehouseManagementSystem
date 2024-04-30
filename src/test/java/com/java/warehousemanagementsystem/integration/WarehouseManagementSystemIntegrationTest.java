package com.java.warehousemanagementsystem.integration;

import com.java.warehousemanagementsystem.WarehouseManagementSystemApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = WarehouseManagementSystemApplication.class)
@AutoConfigureMockMvc
public class WarehouseManagementSystemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCompleteWorkflow() throws Exception {
        // Register
        Map<String, Object> registration = new HashMap<>();
        registration.put("username", "user");
        registration.put("password", "password");
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registration)))
                .andExpect(status().isOk());

        // Login
        Map<String, Object> login = new HashMap<>();
        login.put("username", "user");
        login.put("password", "password");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(login)))
                .andExpect(status().isOk());

        // Add Warehouse
        Map<String, Object> warehouse = new HashMap<>();
        warehouse.put("name", "Warehouse 1");
        warehouse.put("address", "1001 Location St");
        warehouse.put("manager", "Manager");
        mockMvc.perform(post("/warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(warehouse)))
                .andExpect(status().isOk());

        // Add Item, Order, OrderItem, Update, Delete, etc.

        // Logout
        mockMvc.perform(post("/logout"))
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
