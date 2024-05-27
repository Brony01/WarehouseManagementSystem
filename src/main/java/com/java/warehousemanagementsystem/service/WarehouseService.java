package com.java.warehousemanagementsystem.service;


import com.java.warehousemanagementsystem.pojo.Warehouse;

import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Mono;

public interface WarehouseService {
    Mono<Warehouse> addWarehouse(String name, String address, String manager, String description);

    Mono<Boolean> deleteWarehouse(Integer id);

    Mono<Warehouse> updateWarehouse(Integer id, String name, String address, String manager, String description);

    Mono<Warehouse> selectWarehouse(Integer id);

    Mono<PageImpl<Warehouse>> selectWarehouse(String name, Long pageNo, Long pageSize);
}
