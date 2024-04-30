package com.java.warehousemanagementsystem.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WarehouseManagementSystemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // This is where you can set up data for the tests or initialize the database with required values
    }

    @Test
    void testFullScenario() throws Exception {
        // Register a user
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"testPass\",\"confirmedPassword\":\"testPass\"}"))
                .andExpect(status().isOk());

        // Login
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\", \"password\":\"testPass\"}"))
                .andExpect(status().isOk());


        // Add a warehouse
        // Assume your endpoint requires name, address etc., adjust as necessary
        mockMvc.perform(post("/warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Warehouse1\", \"address\":\"1234 Street\", \"manager\":\"Manager\", \"description\":\"Test warehouse\"}"))
                .andExpect(status().isOk());

        // Update the warehouse
        mockMvc.perform(put("/warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\", \"address\":\"1234 Street\", \"manager\":\"Manager\", \"description\":\"Updated description\"}"))
                .andExpect(status().isOk());

        // Get the warehouse
        mockMvc.perform(get("/warehouse/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        // Get all warehouses
        mockMvc.perform(get("/warehouse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("UpdatedName"))
                .andExpect(jsonPath("$[0].description").value("Updated description"));

        // Delete the warehouse
        mockMvc.perform(delete("/warehouse/{id}", 1))
                .andExpect(status().isOk());

        // add an item
        mockMvc.perform(post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Item1\", \"description\":\"Test item\"}"))
                .andExpect(status().isOk());

        // update the item
        mockMvc.perform(put("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedItem\", \"description\":\"Updated item\"}"))
                .andExpect(status().isOk());

        // get the item
        mockMvc.perform(get("/item/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedItem"))
                .andExpect(jsonPath("$.description").value("Updated item"));

        // get all items
        mockMvc.perform(get("/item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("UpdatedItem"))
                .andExpect(jsonPath("$[0].description").value("Updated item"));

        // delete the item
        mockMvc.perform(delete("/item/{id}", 1))
                .andExpect(status().isOk());

        // Add an order
        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"1234 Street\", \"status\":\"Pending\"}"))
                .andExpect(status().isOk());

        // Update the order
        mockMvc.perform(put("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"Updated Street\", \"status\":\"Delivered\"}"))
                .andExpect(status().isOk());

        // Add an item to the order
        mockMvc.perform(post("/order/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":1, \"itemId\":1, \"quantity\":1}"))
                .andExpect(status().isOk());

        // Delete an item from the order
        mockMvc.perform(delete("/order/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":1, \"itemId\":1}"))
                .andExpect(status().isOk());

        // Get the order
        mockMvc.perform(get("/order/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Updated Street"))
                .andExpect(jsonPath("$.status").value("Delivered"));

        // Get all orders
        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].address").value("Updated Street"))
                .andExpect(jsonPath("$[0].status").value("Delivered"));

        // Delete the order
        mockMvc.perform(delete("/order/{id}", 1))
                .andExpect(status().isOk());

        // Get orders by user
        mockMvc.perform(get("/order/user")
                        .param("username", "testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Get orders by status
        mockMvc.perform(get("/order/status/{status}", "Delivered"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Get orders by address
        mockMvc.perform(get("/order/address")
                        .param("address", "Updated Street"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Get orders by item
        mockMvc.perform(get("/order/item/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Logout
        mockMvc.perform(delete("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\"}"))
                .andExpect(status().isOk());
    }
}
