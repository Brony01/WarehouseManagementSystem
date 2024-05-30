package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.mapper.WarehouseMapper;
import com.java.warehousemanagementsystem.pojo.Warehouse;
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

public class WarehouseServiceImplTest {

    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddWarehouse() {
        Warehouse warehouse = new Warehouse();
        when(warehouseMapper.insert(any(Warehouse.class))).thenReturn(1);
        Mono<Boolean> result = warehouseService.addWarehouse("name", "location", "manager", "description").hasElement();
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testUpdateWarehouse() {
        Warehouse warehouse = new Warehouse();
        when(warehouseMapper.updateById(any(Warehouse.class))).thenReturn(1);
//    Mono<Warehouse> updateWarehouse(Integer id, String name, String location, String manager, String description);
        Mono<Warehouse> result = warehouseService.updateWarehouse(1, "name", "location", "manager", "description");
        StepVerifier.create(result)
                .expectNext(warehouse)
                .verifyComplete();
    }

    @Test
    void testFindWarehouseById() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1);
        when(warehouseMapper.selectById(1)).thenReturn(warehouse);

        Mono<Warehouse> result = warehouseService.selectWarehouseById(1);
        StepVerifier.create(result)
                .expectNext(warehouse)
                .verifyComplete();
    }

    @Test
    void testFindAllWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();
        Warehouse warehouse = new Warehouse();
        warehouses.add(warehouse);

        when(warehouseMapper.selectList(any())).thenReturn(warehouses);

        Flux<Warehouse> result = warehouseService.selectWarehouse("", 1L, 10L);
        StepVerifier.create(result)
                .expectNextSequence(warehouses)
                .verifyComplete();
    }

    @Test
    void testDeleteWarehouse() {
        when(warehouseMapper.deleteById(1)).thenReturn(1);
        Mono<Void> result = warehouseService.deleteWarehouse(1);
        StepVerifier.create(result)
                .expectNext()
                .verifyComplete();
    }
}
