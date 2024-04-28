package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.Order;

import java.util.List;

public interface OrderService {
    boolean addOrder(Order order);
    boolean addItem(Integer id, Integer itemId);
    Order findOrderById(Integer id);
    boolean updateOrder(Integer id, Order order);
    boolean deleteOrder(Integer id);
    List<Order> findAllOrders();
    List<Order> findOrdersByUserId(Integer userId);
    List<Order> findOrdersByStatus(String status);
    List<Order> findOrdersByAddress(String address);
}
