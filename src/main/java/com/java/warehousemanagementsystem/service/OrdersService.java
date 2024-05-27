package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrdersService {
    Mono<Boolean> addOrder(Orders orders);

    Mono<Boolean> addItem(Integer id, Integer itemId);

    Mono<Orders> findOrderById(Integer id);

    Mono<Boolean> updateOrder(Integer id, Orders orders);

    Mono<Boolean> deleteOrder(Integer id);

    Mono<Boolean> deleteItem(Integer id, Integer itemId);

    Flux<Orders> findAllOrders();

    Flux<Orders> findOrdersByUsername(String username);

    Flux<Orders> findOrdersByStatus(String status);

    Flux<Orders> findOrdersByAddress(String address);

    Flux<Item> findItemsByOrderId(Integer id);
}
