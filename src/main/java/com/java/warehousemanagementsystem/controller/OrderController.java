package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.Order;
import com.java.warehousemanagementsystem.service.OrderService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private OrderService orderService;

    @Operation(summary = "添加新订单")
    @PostMapping
    @ResponseBody
    public ResponseResult<?> addOrder(@RequestBody @Parameter(description = "订单") Order order) {
        if (orderService.addOrder(order)) {
            logger.info("(OrderController)订单添加成功, ID: {}", order.getId());
            return ResponseResult.success("订单添加成功");
        } else {
            logger.error("(OrderController)订单添加失败");
            return ResponseResult.failure(400, "订单添加失败");
        }
    }

    @Operation(summary = "添加物品")
    @PostMapping("/item/{id}")
    @ResponseBody
    public ResponseResult<?> addItem(@PathVariable @Parameter(description = "订单ID") Integer id,
                                     @RequestBody @Parameter(description = "物品ID") Integer itemId)
    {
        if (orderService.addItem(id, itemId)) {
            logger.info("(OrderController)物品添加成功, ID: {}", id);
            return ResponseResult.success("物品添加成功");
        } else {
            logger.error("(OrderController)物品添加失败");
            return ResponseResult.failure(400, "物品添加失败");
        }
    }

    @Operation(summary = "获取所有订单")
    @GetMapping
    @ResponseBody
    @Cacheable(value = "allOrders")
    public ResponseResult<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAllOrders();
        logger.info("(OrderController)获取订单列表, size = {}", orders.size());
        return ResponseResult.success(orders);
    }

    @Operation(summary = "根据ID获取订单信息")
    @GetMapping("/{id}")
    @ResponseBody
    @Cacheable(value = "order", key = "#id")
    public ResponseResult<Order> getOrderById(@PathVariable @Parameter(description = "订单ID") Integer id) {
        Order order = orderService.findOrderById(id);
        if (order != null) {
            logger.info("(OrderController)订单查找成功, ID: {}", id);
            return ResponseResult.success(order);
        } else {
            logger.error("(OrderController)未找到订单");
            return ResponseResult.failure(404, "未找到订单");
        }
    }

    @Operation(summary = "根据用户ID获取订单信息")
    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseResult<List<Order>> getOrdersByUserId(@PathVariable @Parameter(description = "用户ID") Integer userId) {
        List<Order> orders = orderService.findOrdersByUserId(userId);
        if (orders != null) {
            logger.info("(OrderController)订单查找成功, 用户ID: {}", userId);
            return ResponseResult.success(orders);
        } else {
            logger.error("(OrderController)未找到订单");
            return ResponseResult.failure(404, "未找到订单");
        }
    }

    @Operation(summary = "根据订单状态获取订单信息")
    @GetMapping("/{status}")
    @ResponseBody
    public ResponseResult<List<Order>> getOrdersByStatus(@PathVariable @Parameter(description = "订单状态") String status) {
        List<Order> orders = orderService.findOrdersByStatus(status);
        if (orders != null) {
            logger.info("(OrderController)订单查找成功, 订单状态: {}", status);
            return ResponseResult.success(orders);
        } else {
            logger.error("(OrderController)未找到订单");
            return ResponseResult.failure(404, "未找到订单");
        }
    }

    @Operation(summary = "根据地址获取订单信息")
    @GetMapping("/{address}")
    @ResponseBody
    public ResponseResult<List<Order>> getOrdersByAddress(@PathVariable @Parameter(description = "地址") String address) {
        List<Order> orders = orderService.findOrdersByAddress(address);
        if (orders != null) {
            logger.info("(OrderController)订单查找成功, 地址: {}", address);
            return ResponseResult.success(orders);
        } else {
            logger.error("(OrderController)未找到订单");
            return ResponseResult.failure(404, "未找到订单");
        }
    }

    @Operation(summary = "更新订单信息")
    @PutMapping("/{id}")
    @ResponseBody
    @CachePut(value = "order", key = "#id")
    public ResponseResult<?> updateOrder(@PathVariable @Parameter(description = "订单ID") Integer id, @RequestBody @Parameter(description = "订单") Order order) {
        if (orderService.updateOrder(id, order)) {
            logger.info("(OrderController)订单信息更新成功, ID: {}", id);
            return ResponseResult.success("订单信息更新成功");
        } else {
            logger.error("(OrderController)订单信息更新失败");
            return ResponseResult.failure(400, "订单信息更新失败");
        }
    }

    @Operation(summary = "删除订单")
    @DeleteMapping("/{id}")
    @ResponseBody
    @CacheEvict(value = "order", key = "#id")
    public ResponseResult<?> deleteOrder(@PathVariable @Parameter(description = "订单ID") Integer id) {
        if (orderService.deleteOrder(id)) {
            logger.info("(OrderController)订单删除成功, ID: {}", id);
            return ResponseResult.success("订单删除成功");
        } else {
            logger.error("(OrderController)订单删除失败");
            return ResponseResult.failure(404, "订单删除失败");
        }
    }
}
