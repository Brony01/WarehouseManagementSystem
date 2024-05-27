package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.mapper.OrdersMapper;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import com.java.warehousemanagementsystem.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public Mono<Boolean> addOrder(Orders orders) {
        return Mono.fromCallable(() -> ordersMapper.insert(orders) > 0);
    }

    @Override
    public Mono<Boolean> updateOrder(Orders orders) {
        return Mono.fromCallable(() -> ordersMapper.updateById(orders) > 0);
    }

    @Override
    public Mono<Orders> findOrderById(Integer id) {
        return Mono.fromCallable(() -> {
            Orders orders = ordersMapper.selectById(id);
            if (orders != null) {
                updateOrderTotalPrice(orders.getId());
            }
            return orders;
        });
    }

    @Override
    public Flux<Orders> findOrdersByStatus(String status) {
        return Flux.defer(() -> {
            QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", status);
            updateByOrderList(queryWrapper);
            return Flux.fromIterable(ordersMapper.selectList(queryWrapper));
        });
    }

    @Override
    public Flux<Orders> findOrdersByAddress(String address) {
        return Flux.defer(() -> {
            QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("address", address);
            updateByOrderList(queryWrapper);
            return Flux.fromIterable(ordersMapper.selectList(queryWrapper));
        });
    }

    @Override
    public Flux<Item> findItemsByOrderId(Integer id) {
        return Flux.defer(() -> Flux.fromIterable(ordersMapper.getItemsByOrderId(id)));
    }

    private void updateOrderTotalPrice(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders == null) {
            return;
        }
        List<Item> items = ordersMapper.getItemsByOrderId(id);
        double totalPrice = items.stream().mapToDouble(Item::getPrice).sum();
        orders.setTotalPrice(totalPrice);
        ordersMapper.updateById(orders);
    }

    private void updateByOrderList(QueryWrapper<Orders> queryWrapper) {
        List<Orders> orders = ordersMapper.selectList(queryWrapper);
        orders.forEach(order -> updateOrderTotalPrice(order.getId()));
    }
}
