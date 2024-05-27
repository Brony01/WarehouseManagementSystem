package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrdersService {
    Mono<Boolean> addOrder(Orders orders);
    Mono<Boolean> updateOrder(Orders orders);
    Mono<Orders> findOrderById(Integer id);
    Flux<Orders> findOrdersByStatus(String status);
    Flux<Orders> findOrdersByAddress(String address);
    Flux<Item> findItemsByOrderId(Integer id);
}
