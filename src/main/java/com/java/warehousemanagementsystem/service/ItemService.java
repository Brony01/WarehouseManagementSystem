package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ItemService {
    Mono<Boolean> addItem(String name, String description, Integer quantity, Double price, Integer warehouseId);
    Mono<Boolean> updateItem(Integer id, String name, String description, Integer quantity, Double price, Integer warehouseId);
    Mono<Item> findItemById(Integer id);
    Flux<Item> findAllItems();
    Mono<Boolean> deleteItem(Integer id);
}
