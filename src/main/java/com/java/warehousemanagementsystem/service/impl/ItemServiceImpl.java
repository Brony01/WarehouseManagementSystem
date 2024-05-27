package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.mapper.ItemMapper;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public Mono<Boolean> addItem(String name, String description, Integer quantity, Double price, Integer warehouseId) {
        return Mono.fromCallable(() -> {
            checkItemDetails(name, description, quantity, price);
            Item item = new Item();
            item.setName(name);
            item.setDescription(description);
            item.setQuantity(quantity);
            item.setPrice(price);
            item.setWarehouseId(warehouseId);
            item.setCreateTime(new Date());
            return itemMapper.insert(item) > 0;
        });
    }

    @Override
    public Mono<Boolean> updateItem(Integer id, String name, String description, Integer quantity, Double price, Integer warehouseId) {
        return Mono.fromCallable(() -> {
            checkItemDetails(name, description, quantity, price);
            Item item = itemMapper.selectById(id);
            if (item == null) {
                throw new IllegalArgumentException("Item does not exist");
            }
            item.setName(name);
            item.setDescription(description);
            item.setQuantity(quantity);
            item.setPrice(price);
            item.setWarehouseId(warehouseId);
            item.setUpdateTime(new Date());
            return itemMapper.updateById(item) > 0;
        });
    }

    @Override
    public Mono<Item> findItemById(Integer id) {
        return Mono.fromCallable(() -> {
            if (id == null) {
                throw new IllegalArgumentException("Item ID cannot be null");
            }
            return itemMapper.selectById(id);
        });
    }

    @Override
    public Flux<Item> findAllItems() {
        return Flux.defer(() -> Flux.fromIterable(itemMapper.selectList(null)));
    }

    @Override
    public Mono<Boolean> deleteItem(Integer id) {
        return Mono.fromCallable(() -> {
            if (id == null) {
                throw new IllegalArgumentException("Item ID cannot be null");
            }
            return itemMapper.deleteById(id) > 0;
        });
    }

    private void checkItemDetails(String name, String description, Integer quantity, Double price) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Item name is not correct");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Item description is not correct");
        }
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Item quantity is not correct");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Item price is not correct");
        }
    }
}
