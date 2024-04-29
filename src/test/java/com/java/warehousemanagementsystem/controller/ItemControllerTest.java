package com.java.warehousemanagementsystem.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.java.warehousemanagementsystem.service.ItemService;
import com.java.warehousemanagementsystem.pojo.Item;
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
public class ItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void testGetAllItems() throws Exception {
        List<Item> items = Arrays.asList(new Item(), new Item()); // Create test items
        when(itemService.findAllItems()).thenReturn(items);

        mockMvc.perform(get("/item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(items.size()));

        verify(itemService, times(1)).findAllItems();
    }

    @Test
    public void testAddItem() throws Exception {
        when(itemService.addItem("itemName", "itemDesc", 10, 20.0, 1)).thenReturn(true);

        mockMvc.perform(post("/item")
                        .param("name", "itemName")
                        .param("description", "itemDesc")
                        .param("quantity", "10")
                        .param("price", "20.0")
                        .param("warehouseId", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(itemService, times(1)).addItem("itemName", "itemDesc", 10, 20.0, 1);
    }

    @Test
    public void testGetItemById() throws Exception {
        Item item = new Item(); // Set item properties as needed
        when(itemService.findItemById(1)).thenReturn(item);

        mockMvc.perform(get("/item/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(itemService, times(1)).findItemById(1);
    }

    @Test
    public void testUpdateItem() throws Exception {
        when(itemService.updateItem(1, "itemName", "itemDesc", 10, 20.0, 1)).thenReturn(true);

        mockMvc.perform(put("/item/1")
                        .param("name", "itemName")
                        .param("description", "itemDesc")
                        .param("quantity", "10")
                        .param("price", "20.0")
                        .param("warehouseId", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(itemService, times(1)).updateItem(1, "itemName", "itemDesc", 10, 20.0, 1);
    }

    @Test
    public void testDeleteItem() throws Exception {
        when(itemService.deleteItem(1)).thenReturn(true);

        mockMvc.perform(delete("/item/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(itemService, times(1)).deleteItem(1);
    }
}
