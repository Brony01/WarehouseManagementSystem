package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.OrderItem;
import com.java.warehousemanagementsystem.pojo.Orders;
import com.java.warehousemanagementsystem.repository.OrderItemRepository;
import com.java.warehousemanagementsystem.repository.OrdersRepository;
import com.java.warehousemanagementsystem.service.OrdersService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class OrdersServiceImpl implements OrdersService {
    private static final Logger logger = LoggerFactory.getLogger(OrdersService.class);

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Mono<Boolean> addOrder(Orders orders) {
        orders.setCreateTime(new Date());
        return ordersRepository.save(orders)
                .doOnSuccess(savedOrder -> logger.info("(OrderService) 订单添加成功, ID: {}", savedOrder.getId()))
                .thenReturn(true)
                .onErrorReturn(false);
    }

    @Override
    public Mono<Boolean> addItem(Integer id, Integer itemId) {
        return ordersRepository.findById(id)
                .flatMap(order -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(id);
                    orderItem.setItemId(itemId);
                    return orderItemRepository.save(orderItem)
                            .then(updateOrderTotalPrice(id))
                            .thenReturn(true);
                })
                .doOnSuccess(success -> logger.info("(OrderService) 物品添加成功, ID: {}", id))
                .onErrorReturn(false);
    }

    @Override
    public Mono<Orders> findOrderById(Integer id) {
        return updateOrderTotalPrice(id)
                .then(ordersRepository.findById(id));
    }

    @Override
    @Transactional
    public Mono<Boolean> updateOrder(Integer id, Orders orders) {
        return ordersRepository.findById(id)
                .flatMap(existingOrder -> {
                    orders.setId(id);
                    orders.setUpdateTime(new Date());
                    return ordersRepository.save(orders)
                            .then(updateOrderTotalPrice(id))
                            .thenReturn(true);
                })
                .doOnSuccess(success -> logger.info("(OrderService) 订单更新成功, ID: {}", id))
                .onErrorReturn(false);
    }

    @Override
    @Transactional
    public Mono<Boolean> deleteOrder(Integer id) {
        return ordersRepository.deleteById(id)
                .doOnSuccess(success -> logger.info("(OrderService) 订单删除成功, ID: {}", id))
                .thenReturn(true)
                .onErrorReturn(false);
    }

    @Override
    @Transactional
    public Mono<Boolean> deleteItem(Integer id, Integer itemId) {
        return orderItemRepository.deleteByOrderIdAndItemId(id, itemId)
                .then(updateOrderTotalPrice(id))
                .doOnSuccess(success -> logger.info("(OrderService) 物品删除成功, ID: {}", id))
                .thenReturn(true)
                .onErrorReturn(false);
    }

    @Override
    public Flux<Orders> findAllOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public Flux<Orders> findOrdersByUsername(String username) {
        return ordersRepository.findByUsername(username)
                .doOnNext(order -> updateOrderTotalPrice(order.getId()).subscribe())
                .doOnComplete(() -> logger.info("(OrderService) 根据用户ID查找订单, 用户名: {}", username));
    }

    @Override
    public Flux<Orders> findOrdersByStatus(String status) {
        return ordersRepository.findByStatus(status)
                .doOnNext(order -> updateOrderTotalPrice(order.getId()).subscribe())
                .doOnComplete(() -> logger.info("(OrderService) 根据状态查找订单, 状态: {}", status));
    }

    @Override
    public Flux<Orders> findOrdersByAddress(String address) {
        return ordersRepository.findByAddress(address)
                .doOnNext(order -> updateOrderTotalPrice(order.getId()).subscribe())
                .doOnComplete(() -> logger.info("(OrderService) 根据地址查找订单, 地址: {}", address));
//        return ordersRepository.findByAddressContaining(address)
//                .doOnNext(order -> updateOrderTotalPrice(order.getId()).subscribe())
//                .doOnComplete(() -> logger.info("(OrderService) 根据地址查找订单, 地址: {}", address));
    }

    @Override
    public Flux<Item> findItemsByOrderId(Integer id) {
        return ordersRepository.getItemsByOrderId(id);
    }

    private Mono<Void> updateOrderTotalPrice(Integer id) {
        return ordersRepository.findById(id)
                .flatMap(order -> ordersRepository.getItemsByOrderId(id)
                        .collectList()
                        .flatMap(items -> {
                            double totalPrice = items.stream().mapToDouble(Item::getPrice).sum();
                            order.setTotalPrice(totalPrice);
                            return ordersRepository.save(order).then();
                        }))
                .doOnSuccess(aVoid -> logger.info("(OrderService) 更新订单总价成功, ID: {}", id))
                .then();
    }
}
