package com.java.warehousemanagementsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.warehousemanagementsystem.pojo.Warehouse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WarehouseService {
    Mono<Warehouse> addWarehouse(String name, String location, String manager, String description);
    Mono<Warehouse> updateWarehouse(Integer id, String name, String location, String manager, String description);
    Mono<Void> deleteWarehouse(Integer id);
    Mono<Warehouse> selectWarehouseById(Integer id);
    Flux<Warehouse> selectWarehouse(String name, Long pageNo, Long pageSize);
}
