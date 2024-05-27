package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.Item;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemService {
    Flux<Item> findAllItems();

    Mono<Boolean> addItem(String name, String description, Integer quantity, Double price, Integer warehouseId);

    Mono<Item> findItemById(Integer id);

    Mono<Boolean> updateItem(Integer id, String name, String description, Integer quantity, Double price, Integer warehouseId);

    Mono<Boolean> deleteItem(Integer id);
}

