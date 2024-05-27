package com.java.warehousemanagementsystem.repository;

import com.java.warehousemanagementsystem.pojo.OrderItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Integer> {
    Mono<Void> deleteByOrderIdAndItemId(Integer orderId, Integer itemId);

    Flux<Object> findByOrderId(Integer id);
}

