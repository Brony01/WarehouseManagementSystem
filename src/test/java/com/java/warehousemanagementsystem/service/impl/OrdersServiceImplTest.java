package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.mapper.OrderItemMapper;
import com.java.warehousemanagementsystem.mapper.OrdersMapper;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.OrderItem;
import com.java.warehousemanagementsystem.pojo.Orders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrdersServiceImplTest {

    @Mock
    private OrdersMapper ordersMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addOrderSuccessfully() {
        Orders order = new Orders();
        when(ordersMapper.insert(order)).thenReturn(1);

        assertTrue(ordersService.addOrder(order));
        verify(ordersMapper, times(1)).insert(any(Orders.class));
    }

    @Test
    void addOrderFailure() {
        Orders order = new Orders();
        when(ordersMapper.insert(order)).thenThrow(new RuntimeException("Insert failed"));

        assertFalse(ordersService.addOrder(order));
        verify(ordersMapper, times(1)).insert(any(Orders.class));
    }

    @Test
    void updateOrderSuccessfully() {
        Integer orderId = 1;
        Orders order = new Orders();
        order.setId(orderId);

        // Mock the behavior of ordersMapper
        when(ordersMapper.updateById(any(Orders.class))).thenReturn(1);
        when(ordersMapper.selectById(orderId)).thenReturn(order);
        when(ordersMapper.getItemsByOrderId(orderId)).thenReturn(new ArrayList<>()); // Assuming no items for simplicity

        // Call the method under test
        assertTrue(ordersService.updateOrder(orderId, order));

        // Verify that updateById was called twice as expected
        verify(ordersMapper, times(2)).updateById(order);
        // First call is from the updateOrder method directly
        // Second call is from the updateOrderTotalPrice method
    }


    @Test
    void deleteOrderSuccessfully() {
        when(ordersMapper.deleteById(1)).thenReturn(1);

        assertTrue(ordersService.deleteOrder(1));
    }

    @Test
    void addItemToOrderSuccessfully() {
        Integer orderId = 1;
        Integer itemId = 1;
        when(orderItemMapper.insert(any(OrderItem.class))).thenReturn(1);
        Orders order = new Orders();
        order.setId(orderId);
        when(ordersMapper.selectById(orderId)).thenReturn(order);
        when(ordersMapper.getItemsByOrderId(orderId)).thenReturn(new ArrayList<>());

        assertTrue(ordersService.addItem(orderId, itemId));
        verify(orderItemMapper, times(1)).insert(any(OrderItem.class));
    }

    @Test
    void deleteItemFromOrderSuccessfully() {
        Integer orderId = 1;
        Integer itemId = 1;
        when(orderItemMapper.delete(any())).thenReturn(1);
        Orders order = new Orders();
        order.setId(orderId);
        when(ordersMapper.selectById(orderId)).thenReturn(order);
        when(ordersMapper.getItemsByOrderId(orderId)).thenReturn(new ArrayList<>());

        assertTrue(ordersService.deleteItem(orderId, itemId));
        verify(orderItemMapper, times(1)).delete(any());
    }

    @Test
    void findOrderById() {
        Orders order = new Orders();
        order.setId(1);
        when(ordersMapper.selectById(1)).thenReturn(order);

        assertEquals(order, ordersService.findOrderById(1));
    }

    @Test
    void findAllOrders() {
        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(new Orders());
        when(ordersMapper.selectList(null)).thenReturn(ordersList);

        assertEquals(1, ordersService.findAllOrders().size());
    }

    @Test
    void findOrdersByUsername() {
        String username = "user";
        List<Orders> ordersList = new ArrayList<>();
        Orders order = new Orders();
        order.setId(1);
        ordersList.add(order);

        // Set up the mock responses
        when(ordersMapper.selectList(any())).thenReturn(ordersList);
        when(ordersMapper.selectById(1)).thenReturn(order);
        when(ordersMapper.getItemsByOrderId(1)).thenReturn(new ArrayList<>());

        // Invoke the method under test
        List<Orders> result = ordersService.findOrdersByUsername(username);

        // Assertions
        assertEquals(1, result.size(), "Expected one order for the given username");

        // Verification that selectList is called at least once, but can be more depending on internal logic
        verify(ordersMapper, atLeastOnce()).selectList(any());
    }


    @Test
    void findOrdersByStatus() {
        String status = "pending";
        List<Orders> ordersList = new ArrayList<>();
        Orders order = new Orders();
        order.setId(1);
        ordersList.add(order);

        // Mock setup
        when(ordersMapper.selectList(any())).thenReturn(ordersList);
        when(ordersMapper.selectById(1)).thenReturn(order);
        when(ordersMapper.getItemsByOrderId(1)).thenReturn(new ArrayList<>());

        // Method invocation
        List<Orders> result = ordersService.findOrdersByStatus(status);

        // Assertions
        assertEquals(1, result.size(), "Expected one order for the given status");

        // Verification that selectList is called at least once, but may be more
        verify(ordersMapper, atLeastOnce()).selectList(any());
    }


    @Test
    void findOrdersByAddress() {
        String address = "123 Main St";
        List<Orders> ordersList = new ArrayList<>();
        Orders order = new Orders();
        order.setId(1);
        ordersList.add(order);

        // Mock setup
        when(ordersMapper.selectList(any())).thenReturn(ordersList);
        when(ordersMapper.selectById(1)).thenReturn(order);
        when(ordersMapper.getItemsByOrderId(1)).thenReturn(new ArrayList<>());

        // Method invocation
        List<Orders> result = ordersService.findOrdersByAddress(address);

        // Assertions
        assertEquals(1, result.size(), "Expected one order for the given address");

        // Verification that selectList is called at least once, but may be more
        verify(ordersMapper, atLeastOnce()).selectList(any());
    }


    @Test
    void findItemsByOrderId() {
        List<Item> items = new ArrayList<>();
        items.add(new Item());
        when(ordersMapper.getItemsByOrderId(1)).thenReturn(items);

        assertEquals(1, ordersService.findItemsByOrderId(1).size());
    }
}
