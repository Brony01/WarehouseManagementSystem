package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.mapper.ItemMapper;
import com.java.warehousemanagementsystem.pojo.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ItemServiceImplTest {

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddItem() {
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setQuantity(10);
        item.setPrice(20.0);
        item.setWarehouseId(1);
        item.setCreateTime(new Date());

        when(itemMapper.insert(any(Item.class))).thenReturn(1);

        Mono<Boolean> result = itemService.addItem("Test Item", "Test Description", 10, 20.0, 1);
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testUpdateItem() {
        Item item = new Item();
        item.setId(1);
        item.setName("Updated Item");
        item.setDescription("Updated Description");
        item.setQuantity(5);
        item.setPrice(15.0);
        item.setWarehouseId(1);
        item.setUpdateTime(new Date());

        when(itemMapper.selectById(1)).thenReturn(item);
        when(itemMapper.updateById(any(Item.class))).thenReturn(1);

        Mono<Boolean> result = itemService.updateItem(1, "Updated Item", "Updated Description", 5, 15.0, 1);
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testFindItemById() {
        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");
        item.setDescription("Test Description");

        when(itemMapper.selectById(1)).thenReturn(item);

        Mono<Item> result = itemService.findItemById(1);
        StepVerifier.create(result)
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    void testDeleteItem() {
        when(itemMapper.deleteById(1)).thenReturn(1);

        Mono<Boolean> result = itemService.deleteItem(1);
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testFindAllItems() {
        List<Item> expectedItems = new ArrayList<>();
        expectedItems.add(new Item());
        expectedItems.add(new Item());

        when(itemMapper.selectList(null)).thenReturn(expectedItems);

        Flux<Item> result = itemService.findAllItems();
        StepVerifier.create(result)
                .expectNextSequence(expectedItems)
                .verifyComplete();
    }

    @Test
    void testAddItemWithInvalidParameters() {
        Mono<Boolean> result = itemService.addItem(null, "description", 10, 20.0, 1);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Item name cannot be null"))
                .verify();

        result = itemService.addItem("Item", "", 10, 20.0, 1);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Item description cannot be empty"))
                .verify();

        result = itemService.addItem("Item", "description", -1, 20.0, 1);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Invalid item quantity"))
                .verify();

        result = itemService.addItem("Item", "description", 10, -20.0, 1);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Invalid item price"))
                .verify();
    }

    @Test
    void testUpdateItemWithInvalidParameters() {
        Mono<Boolean> result = itemService.updateItem(1, null, "description", 10, 20.0, 1);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Item name cannot be null"))
                .verify();

        result = itemService.updateItem(1, "Item", "", 10, 20.0, 1);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Item description cannot be empty"))
                .verify();

        result = itemService.updateItem(1, "Item", "description", -1, 20.0, 1);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Invalid item quantity"))
                .verify();

        result = itemService.updateItem(1, "Item", "description", 10, -20.0, 1);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Invalid item price"))
                .verify();
    }

    @Test
    void testFindItemByIdWithInvalidId() {
        Integer invalidId = null;
        Mono<Item> result = itemService.findItemById(invalidId);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Item ID cannot be null"))
                .verify();
    }

    @Test
    void testDeleteItemWithInvalidId() {
        Integer invalidId = null;
        Mono<Boolean> result = itemService.deleteItem(invalidId);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Item ID cannot be null"))
                .verify();
    }
}
