package com.java.warehousemanagementsystem.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import com.java.warehousemanagementsystem.service.OrdersService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
public class OrdersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrdersService ordersService;

    @InjectMocks
    private OrdersController ordersController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ordersController).build();
    }

    @Test
    public void testAddOrder() throws Exception {
        Orders order = new Orders(); // Initialize with test data as necessary
        when(ordersService.addOrder(order)).thenReturn(true);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"details\": \"Test order\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(ordersService, times(1)).addOrder(any(Orders.class));
    }

    @Test
    public void testAddItem() throws Exception {
        when(ordersService.addItem(1, 100)).thenReturn(true);

        mockMvc.perform(post("/order/item/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": 100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(ordersService, times(1)).addItem(1, 100);
    }

    @Test
    public void testGetAllOrders() throws Exception {
        List<Orders> orders = Arrays.asList(new Orders(), new Orders());
        when(ordersService.findAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(orders.size()));

        verify(ordersService, times(1)).findAllOrders();
    }

    @Test
    public void testGetOrderById() throws Exception {
        Orders order = new Orders();
        List<Item> items = Arrays.asList(new Item(), new Item());
        when(ordersService.findOrderById(1)).thenReturn(order);
        when(ordersService.findItemsByOrderId(1)).thenReturn(items);

        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.order").exists())
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items.size()").value(items.size()));

        verify(ordersService, times(1)).findOrderById(1);
        verify(ordersService, times(1)).findItemsByOrderId(1);
    }

    @Test
    public void testGetOrdersByUserId() throws Exception {
        List<Orders> orders = Arrays.asList(new Orders(), new Orders()); // Assume initialization with test data
        when(ordersService.findOrdersByUserId(123)).thenReturn(orders);

        mockMvc.perform(get("/order/user/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(orders.size()));

        verify(ordersService, times(1)).findOrdersByUserId(123);
    }

    @Test
    public void testGetOrdersByStatus() throws Exception {
        List<Orders> orders = Arrays.asList(new Orders(), new Orders()); // Assume initialization with test data
        when(ordersService.findOrdersByStatus("shipped")).thenReturn(orders);

        mockMvc.perform(get("/order/status/shipped"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(orders.size()));

        verify(ordersService, times(1)).findOrdersByStatus("shipped");
    }

    @Test
    public void testGetOrdersByAddress() throws Exception {
        List<Orders> orders = Arrays.asList(new Orders(), new Orders()); // Assume initialization with test data
        when(ordersService.findOrdersByAddress("123 Main St")).thenReturn(orders);

        mockMvc.perform(get("/order/address/123 Main St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(orders.size()));

        verify(ordersService, times(1)).findOrdersByAddress("123 Main St");
    }

    @Test
    public void testUpdateOrder() throws Exception {
        when(ordersService.updateOrder(eq(1), any(Orders.class))).thenReturn(true);

        mockMvc.perform(put("/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"details\": \"Updated order\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(ordersService, times(1)).updateOrder(eq(1), any(Orders.class));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        when(ordersService.deleteOrder(1)).thenReturn(true);

        mockMvc.perform(delete("/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(ordersService, times(1)).deleteOrder(1);
    }

    @Test
    public void testDeleteItem() throws Exception {
        when(ordersService.deleteItem(1, 100)).thenReturn(true);

        mockMvc.perform(delete("/order/item/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": 100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(ordersService, times(1)).deleteItem(1, 100);
    }
}
