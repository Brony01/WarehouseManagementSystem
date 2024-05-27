package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.repository.ItemRepository;
import com.java.warehousemanagementsystem.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Mono<Boolean> addItem(String name, String description, Integer quantity, Double price, Integer warehouseId) {
        checkItemDetails(name, description, quantity, price);

        return itemRepository.findByNameAndWarehouseId(name, warehouseId)
                .flatMap(existingItem -> {
                    logger.error("(ItemService)同一仓库中物品已存在");
                    return Mono.error(new IllegalArgumentException("同一仓库中物品已存在"));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    Date currentTime = new Date();
                    Item item = new Item(null, name, description, quantity, price, warehouseId, currentTime, null);
                    return itemRepository.save(item)
                            .doOnSuccess(savedItem -> logger.info("(ItemService)物品添加成功, id = {}. name = {}, description = {}, quantity = {}, price = {}, warehouseId = {}, createTime = {}",
                                    savedItem.getId(), savedItem.getName(), savedItem.getDescription(), savedItem.getQuantity(), savedItem.getPrice(), savedItem.getWarehouseId(), savedItem.getCreateTime()))
                            .thenReturn(true);
                })).hasElement();
    }

    private void checkItemDetails(String name, String description, Integer quantity, Double price) {
        if (name == null || name.trim().isEmpty()) {
            logger.error("(ItemService)物品名称不能为空");
            throw new IllegalArgumentException("物品名称不能为空");
        }

        if (description == null || description.trim().isEmpty()) {
            logger.error("(ItemService)物品描述不能为空");
            throw new IllegalArgumentException("物品描述不能为空");
        }

        if (quantity == null || quantity < 0) {
            logger.error("(ItemService)物品数量不正确");
            throw new IllegalArgumentException("物品数量不正确");
        }

        if (price == null || price < 0) {
            logger.error("(ItemService)物品价格不正确");
            throw new IllegalArgumentException("物品价格不正确");
        }
    }

    @Override
    public Mono<Boolean> updateItem(Integer id, String name, String description, Integer quantity, Double price, Integer warehouseId) {
        checkItemDetails(name, description, quantity, price);

        return itemRepository.findById(id)
                .flatMap(item -> {
                    item.setName(name);
                    item.setDescription(description);
                    item.setQuantity(quantity);
                    item.setPrice(price);
                    item.setWarehouseId(warehouseId);
                    item.setUpdateTime(new Date());
                    return itemRepository.save(item)
                            .doOnSuccess(updatedItem -> logger.info("(ItemService)物品信息更新成功, id = {}. name = {}, description = {}, quantity = {}, price = {}, warehouseId = {}, updateTime = {}",
                                    updatedItem.getId(), updatedItem.getName(), updatedItem.getDescription(), updatedItem.getQuantity(), updatedItem.getPrice(), updatedItem.getWarehouseId(), updatedItem.getUpdateTime()))
                            .thenReturn(true);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.error("(ItemService)物品不存在");
                    return Mono.error(new IllegalArgumentException("物品不存在"));
                }));
    }

    @Override
    public Mono<Item> findItemById(Integer id) {
        if (id == null) {
            logger.error("(ItemService)物品ID不能为空");
            throw new IllegalArgumentException("物品ID不能为空");
        }
        return itemRepository.findById(id);
    }

    @Override
    public Flux<Item> findAllItems() {
        return itemRepository.findAll()
                .doOnNext(item -> logger.info("(ItemService)获取物品, id = {}, name = {}", item.getId(), item.getName()));
    }

    @Override
    public Mono<Boolean> deleteItem(Integer id) {
        if (id == null) {
            logger.error("(ItemService)物品ID不能为空");
            throw new IllegalArgumentException("物品ID不能为空");
        }
        return itemRepository.findById(id)
                .flatMap(item -> itemRepository.delete(item)
                        .then(Mono.just(true)))
                .switchIfEmpty(Mono.defer(() -> {
                    logger.error("(ItemService)物品不存在");
                    return Mono.error(new IllegalArgumentException("物品不存在"));
                }));
    }
}
