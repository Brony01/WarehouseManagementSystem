package com.java.warehousemanagementsystem.repository;

import com.java.warehousemanagementsystem.pojo.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ItemRepository extends ReactiveCrudRepository<Item, Integer> {
    Mono<Object> findByNameAndWarehouseId(String name, Integer warehouseId);
}

