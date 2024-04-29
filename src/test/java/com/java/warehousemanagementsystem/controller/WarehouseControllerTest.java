package com.java.warehousemanagementsystem.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.warehousemanagementsystem.pojo.Warehouse;
import com.java.warehousemanagementsystem.service.WarehouseService;
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
public class WarehouseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private WarehouseController warehouseController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(warehouseController).build();
    }

    @Test
    public void testCreateWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse(); // Initialize with test data as necessary
        when(warehouseService.addWarehouse(anyString(), anyString(), anyString(), anyString())).thenReturn(warehouse);

        mockMvc.perform(post("/warehouse")
                        .param("name", "Main Warehouse")
                        .param("location", "Downtown")
                        .param("manager", "Alice")
                        .param("description", "Central storage facility")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());

        verify(warehouseService, times(1)).addWarehouse("Main Warehouse", "Downtown", "Alice", "Central storage facility");
    }

    @Test
    public void testUpdateWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse(); // Initialize with test data as necessary
        when(warehouseService.updateWarehouse(anyInt(), anyString(), anyString(), anyString(), anyString())).thenReturn(warehouse);

        mockMvc.perform(put("/warehouse/1")
                        .param("name", "Updated Warehouse")
                        .param("location", "Uptown")
                        .param("manager", "Bob")
                        .param("description", "Updated facility")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());

        verify(warehouseService, times(1)).updateWarehouse(1, "Updated Warehouse", "Uptown", "Bob", "Updated facility");
    }

    @Test
    public void testFindWarehouseById() throws Exception {
        Warehouse warehouse = new Warehouse(); // Assume initialized
        when(warehouseService.selectWarehouse(anyInt())).thenReturn(warehouse);

        mockMvc.perform(get("/warehouse/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());

        verify(warehouseService, times(1)).selectWarehouse(1);
    }

    @Test
    public void testGetWarehouseList() throws Exception {
        Page<Warehouse> page = new Page<>(); // Assume initialization and setting data
        when(warehouseService.selectWarehouse(anyString(), anyLong(), anyLong())).thenReturn(page);

        mockMvc.perform(get("/warehouse")
                        .param("name", "Main")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());

        verify(warehouseService, times(1)).selectWarehouse("Main", 1L, 10L);
    }

    @Test
    public void testDeleteWarehouse() throws Exception {
        when(warehouseService.deleteWarehouse(1)).thenReturn(true);

        mockMvc.perform(delete("/warehouse/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(warehouseService, times(1)).deleteWarehouse(1);
    }
}
