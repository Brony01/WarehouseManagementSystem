package com.java.warehousemanagementsystem.repository;

import com.java.warehousemanagementsystem.pojo.Warehouse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface WarehouseRepository extends ReactiveCrudRepository<Warehouse, Integer> {
    Flux<Warehouse> findByNameContaining(String name, Pageable pageable);
}
