package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.mapper.OrderItemMapper;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import com.java.warehousemanagementsystem.mapper.OrdersMapper;
import com.java.warehousemanagementsystem.pojo.OrderItem;
import com.java.warehousemanagementsystem.service.OrdersService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrdersServiceImpl implements OrdersService
{
    private static final Logger logger = LoggerFactory.getLogger(OrdersService.class);

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public boolean addOrder(Orders orders) {
        try {
            orders.setCreateTime(new Date());
            ordersMapper.insert(orders);
            logger.info("(OrderService) 订单添加成功, ID: {}", orders.getId());
            return true;
        } catch (Exception e) {
            logger.error("(OrderService) 添加订单失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addItem(Integer id, Integer itemId)
    {
        try {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(id);
            orderItem.setItemId(itemId);
            orderItemMapper.insert(orderItem);
            updateOrderTotalPrice(id);
            logger.info("(OrderService) 物品添加成功, ID: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("(OrderService) 物品添加失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Orders findOrderById(Integer id) {
        updateOrderTotalPrice(id);
        return ordersMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean updateOrder(Integer id, Orders orders) {
        try {
            orders.setId(id);  // Ensure the order has the correct ID
            orders.setUpdateTime(new Date());
            ordersMapper.updateById(orders);
            updateOrderTotalPrice(id);
            logger.info("(OrderService) 订单更新成功, ID: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("(OrderService) 更新订单失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteOrder(Integer id) {
        try {
            ordersMapper.deleteById(id);
            logger.info("(OrderService) 订单删除成功, ID: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("(OrderService) 删除订单失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteItem(Integer id, Integer itemId) {
        try {
            QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", id);
            queryWrapper.eq("item_id", itemId);
            orderItemMapper.delete(queryWrapper);
            updateOrderTotalPrice(id);
            logger.info("(OrderService) 物品删除成功, ID: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("(OrderService) 删除物品失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<Orders> findAllOrders()
    {
        return ordersMapper.selectList(null);
    }

    @Override
    public List<Orders> findOrdersByUsername(String username) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        updateByOrderList(queryWrapper);
        logger.info("(OrderService) 根据用户ID查找订单, ID: {}", username);
        return ordersMapper.selectList(queryWrapper);
    }

    @Override
    public List<Orders> findOrdersByStatus(String status) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        updateByOrderList(queryWrapper);
        logger.info("(OrderService) 根据状态查找订单, 状态: {}", status);
        return ordersMapper.selectList(queryWrapper);
    }

    @Override
    public List<Orders> findOrdersByAddress(String address) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("address", address);
        updateByOrderList(queryWrapper);
        logger.info("(OrderService) 根据地址查找订单, 地址: {}", address);
        return ordersMapper.selectList(queryWrapper);
    }

    @Override
    public List<Item> findItemsByOrderId(Integer id)
    {
        return ordersMapper.getItemsByOrderId(id);
    }

    private void updateOrderTotalPrice(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders == null) {
            logger.error("(OrderService) No order found with ID: {}", id);
            return; // Early exit if no order is found
        }
        List<Item> items = ordersMapper.getItemsByOrderId(id);
        double totalPrice = 0;
        for (Item item : items) {
            totalPrice += item.getPrice();
        }
        orders.setTotalPrice(totalPrice);
        ordersMapper.updateById(orders);
    }


    private void updateByOrderList(QueryWrapper<Orders> queryWrapper)
    {
        List<Orders> orders = ordersMapper.selectList(queryWrapper);
        for (Orders order : orders)
        {
            updateOrderTotalPrice(order.getId());
        }
    }
}
