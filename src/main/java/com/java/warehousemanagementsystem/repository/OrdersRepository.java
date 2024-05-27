package com.java.warehousemanagementsystem.repository;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrdersRepository extends ReactiveCrudRepository<Orders, Integer> {
    Flux<Orders> findByUsername(String username);

    Flux<Orders> findByStatus(String status);

    Flux<Orders> findByAddress(String address);

    Flux<Object> findByAddressContaining(String address);

    Flux<Item> getItemsByOrderId(Integer id);
}
