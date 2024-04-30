package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.enums.AppHttpCodeEnum;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import com.java.warehousemanagementsystem.service.OrdersService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdersControllerTest {

    @Mock
    private OrdersService ordersService;

    @InjectMocks
    private OrdersController ordersController;

    @Captor
    private ArgumentCaptor<Orders> orderArgumentCaptor;


    @Test
    void testAddOrderSuccess() {
        String username = "testUser";
        String address = "123 Main St";

        when(ordersService.addOrder(any(Orders.class))).thenReturn(true);

        ResponseResult<?> result = ordersController.addOrder(username, address);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());

        verify(ordersService).addOrder(orderArgumentCaptor.capture());
        Orders capturedOrder = orderArgumentCaptor.getValue();
        assertEquals(username, capturedOrder.getUsername());
        assertEquals(address, capturedOrder.getAddress());
    }

    @Test
    void testAddOrderFailure() {
        String username = "testUser";
        String address = "123 Main St";

        when(ordersService.addOrder(any(Orders.class))).thenReturn(false);

        ResponseResult<?> result = ordersController.addOrder(username, address);

        assertEquals(AppHttpCodeEnum.BAD_REQUEST.getCode(), result.getCode());
        assertEquals("订单添加失败", result.getMsg());

        verify(ordersService).addOrder(orderArgumentCaptor.capture());
        Orders capturedOrder = orderArgumentCaptor.getValue();
        assertEquals(username, capturedOrder.getUsername());
        assertEquals(address, capturedOrder.getAddress());
    }

    @Test
    void testAddItem() {
        when(ordersService.addItem(1, 10)).thenReturn(true);

        ResponseResult<?> result = ordersController.addItem(1, 10);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    void testGetAllOrders() {
        List<Orders> orders = Arrays.asList(new Orders(), new Orders());
        when(ordersService.findAllOrders()).thenReturn(orders);

        ResponseResult<List<Orders>> result = ordersController.getAllOrders();

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testGetOrderById() {
        Orders order = new Orders();
        List<Item> items = Arrays.asList(new Item(), new Item());
        when(ordersService.findOrderById(1)).thenReturn(order);
        when(ordersService.findItemsByOrderId(1)).thenReturn(items);

        ResponseResult<Map<String, Object>> result = ordersController.getOrderById(1);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertNotNull(result.getData().get("order"));
        assertEquals(2, ((List<?>) result.getData().get("items")).size());
    }

    @Test
    void testUpdateOrder() {
        Orders order = new Orders();
        when(ordersService.updateOrder(1, order)).thenReturn(true);

        ResponseResult<?> result = ordersController.updateOrder(1, order);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    void testDeleteOrder() {
        when(ordersService.deleteOrder(1)).thenReturn(true);

        ResponseResult<?> result = ordersController.deleteOrder(1);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    void testDeleteItem() {
        when(ordersService.deleteItem(1, 10)).thenReturn(true);

        ResponseResult<?> result = ordersController.deleteItem(1, 10);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    void testGetOrdersByUserId() {
        List<Orders> orders = Arrays.asList(new Orders(), new Orders());
        when(ordersService.findOrdersByUsername(String.valueOf(1))).thenReturn(orders);

        ResponseResult<List<Orders>> result = ordersController.getOrdersByUserId(String.valueOf(1));

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testGetOrdersByStatus() {
        List<Orders> orders = Arrays.asList(new Orders(), new Orders());
        when(ordersService.findOrdersByStatus("processed")).thenReturn(orders);

        ResponseResult<List<Orders>> result = ordersController.getOrdersByStatus("processed");

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testGetOrdersByAddress() {
        List<Orders> orders = Arrays.asList(new Orders(), new Orders());
        when(ordersService.findOrdersByAddress("123 Main St")).thenReturn(orders);

        ResponseResult<List<Orders>> result = ordersController.getOrdersByAddress("123 Main St");

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals(2, result.getData().size());
    }
}
