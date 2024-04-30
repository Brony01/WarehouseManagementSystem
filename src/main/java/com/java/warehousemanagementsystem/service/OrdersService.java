package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;

import java.util.List;

public interface OrdersService {
    boolean addOrder(Orders orders);

    boolean addItem(Integer id, Integer itemId);

    Orders findOrderById(Integer id);

    boolean updateOrder(Integer id, Orders orders);

    boolean deleteOrder(Integer id);

    boolean deleteItem(Integer id, Integer itemId);

    List<Orders> findAllOrders();

    List<Orders> findOrdersByUsername(String username);

    List<Orders> findOrdersByStatus(String status);

    List<Orders> findOrdersByAddress(String address);

    List<Item> findItemsByOrderId(Integer id);
}
