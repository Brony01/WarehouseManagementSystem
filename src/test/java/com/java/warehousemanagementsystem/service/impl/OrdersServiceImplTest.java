package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.mapper.OrdersMapper;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrdersServiceImplTest {

    @Mock
    private OrdersMapper ordersMapper;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrder() {
        Orders order = new Orders();
        when(ordersMapper.insert(any(Orders.class))).thenReturn(1);

        Mono<Boolean> result = ordersService.addOrder(order);
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testUpdateOrder() {
        Orders order = new Orders();
        when(ordersMapper.updateById(any(Orders.class))).thenReturn(1);

        Mono<Boolean> result = ordersService.updateOrder(order);
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testFindOrderById() {
        Orders order = new Orders();
        order.setId(1);
        when(ordersMapper.selectById(1)).thenReturn(order);

        Mono<Orders> result = ordersService.findOrderById(1);
        StepVerifier.create(result)
                .expectNext(order)
                .verifyComplete();
    }

    @Test
    void testFindOrdersByStatus() {
        List<Orders> ordersList = new ArrayList<>();
        Orders order = new Orders();
        ordersList.add(order);

        when(ordersService.findOrdersByStatus(any())).thenReturn((Flux<Orders>) ordersList);

        Flux<Orders> result = ordersService.findOrdersByStatus("pending");
        StepVerifier.create(result)
                .expectNextSequence(ordersList)
                .verifyComplete();
    }

    @Test
    void testFindOrdersByAddress() {
        List<Orders> ordersList = new ArrayList<>();
        Orders order = new Orders();
        ordersList.add(order);

        when(ordersService.findOrdersByAddress(any())).thenReturn((Flux<Orders>) ordersList);

        Flux<Orders> result = ordersService.findOrdersByAddress("123 Main St");
        StepVerifier.create(result)
                .expectNextSequence(ordersList)
                .verifyComplete();
    }

    @Test
    void testFindItemsByOrderId() {
        List<Item> items = new ArrayList<>();
        items.add(new Item());

        when(ordersMapper.getItemsByOrderId(1)).thenReturn(items);

        Flux<Item> result = ordersService.findItemsByOrderId(1);
        StepVerifier.create(result)
                .expectNextSequence(items)
                .verifyComplete();
    }
}
