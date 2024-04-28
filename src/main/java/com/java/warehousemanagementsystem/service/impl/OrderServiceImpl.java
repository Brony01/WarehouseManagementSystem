package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.pojo.Order;
import com.java.warehousemanagementsystem.mapper.OrderMapper;
import com.java.warehousemanagementsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public boolean addOrder(Order order) {
        try {
            orderMapper.insert(order);
            logger.info("(OrderService) 订单添加成功, ID: {}", order.getId());
            return true;
        } catch (Exception e) {
            logger.error("(OrderService) 添加订单失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Order findOrderById(Integer id) {
        return orderMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean updateOrder(Integer id, Order order) {
        try {
            order.setId(id);  // Ensure the order has the correct ID
            orderMapper.updateById(order);
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
            orderMapper.deleteById(id);
            logger.info("(OrderService) 订单删除成功, ID: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("(OrderService) 删除订单失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<Order> findAllOrders() {
        return orderMapper.selectList(null);
    }

    @Override
    public List<Order> findOrdersByUserId(Integer userId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        logger.info("(OrderService) 根据用户ID查找订单, ID: {}", userId);
        return orderMapper.selectList(queryWrapper);
    }

    @Override
    public List<Order> findOrdersByStatus(String status) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        logger.info("(OrderService) 根据状态查找订单, 状态: {}", status);
        return orderMapper.selectList(queryWrapper);
    }

    @Override
    public List<Order> findOrdersByAddress(String address) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("address", address);
        logger.info("(OrderService) 根据地址查找订单, 地址: {}", address);
        return orderMapper.selectList(queryWrapper);
    }
}
