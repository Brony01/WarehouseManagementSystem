package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import com.java.warehousemanagementsystem.service.OrdersService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/order")
public class OrdersController
{
    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Resource
    private OrdersService ordersService;

    @Operation(summary = "添加新订单")
    @PostMapping
    @ResponseBody
    public ResponseResult<?> addOrder(@RequestBody @Parameter(description = "订单") Orders orders) {
        if (ordersService.addOrder(orders)) {
            logger.info("(OrderController)订单添加成功, ID: {}", orders.getId());
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
        if (ordersService.addItem(id, itemId)) {
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
    public ResponseResult<List<Orders>> getAllOrders() {
        List<Orders> orders = ordersService.findAllOrders();
        logger.info("(OrderController)获取订单列表, size = {}", orders.size());
        return ResponseResult.success(orders);
    }

    @Operation(summary = "根据ID获取订单信息及其物品")
    @GetMapping("/{id}")
    @ResponseBody
    @Cacheable(value = "order", key = "#id")
    public ResponseResult<Map<String, Object>> getOrderById(@PathVariable @Parameter(description = "订单ID") Integer id) {
        Orders orders = ordersService.findOrderById(id);
        List<Item> items = ordersService.findItemsByOrderId(id);

        if (orders != null) {
            logger.info("(OrderController)订单查找成功, ID: {}", id);
            Map<String, Object> result = new HashMap<>();
            result.put("order", orders);
            if (items != null) {
                logger.info("(OrderController)物品查找成功, 订单ID: {}", id);
                result.put("items", items);
            } else {
                logger.warn("(OrderController)未找到物品, 订单ID: {}", id);
            }
            return ResponseResult.success(result);
        } else {
            logger.error("(OrderController)未找到订单, ID: {}", id);
            return ResponseResult.failure(404, "未找到订单");
        }
    }


    @Operation(summary = "根据用户ID获取订单信息")
    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseResult<List<Orders>> getOrdersByUserId(@PathVariable @Parameter(description = "用户ID") Integer userId) {
        List<Orders> orders = ordersService.findOrdersByUserId(userId);
        if (orders != null) {
            logger.info("(OrderController)订单查找成功, 用户ID: {}", userId);
            return ResponseResult.success(orders);
        } else {
            logger.error("(OrderController)未找到订单");
            return ResponseResult.failure(404, "未找到订单");
        }
    }

    @Operation(summary = "根据订单状态获取订单信息")
    @GetMapping("/status/{status}")
    @ResponseBody
    public ResponseResult<List<Orders>> getOrdersByStatus(@PathVariable @Parameter(description = "订单状态") String status) {
        List<Orders> orders = ordersService.findOrdersByStatus(status);
        if (orders != null) {
            logger.info("(OrderController)订单查找成功, 订单状态: {}", status);
            return ResponseResult.success(orders);
        } else {
            logger.error("(OrderController)未找到订单");
            return ResponseResult.failure(404, "未找到订单");
        }
    }

    @Operation(summary = "根据地址获取订单信息")
    @GetMapping("/address/{address}")
    @ResponseBody
    public ResponseResult<List<Orders>> getOrdersByAddress(@PathVariable @Parameter(description = "地址") String address) {
        List<Orders> orders = ordersService.findOrdersByAddress(address);
        if (orders != null) {
            logger.info("(OrderController)订单查找成功, 地址: {}", address);
            return ResponseResult.success(orders);
        } else {
            logger.error("(OrderController)未找到订单");
            return ResponseResult.failure(404, "未找到订单");
        }
    }

//    @Operation(summary = "根据订单查找物品")
//    @GetMapping("/item/{id}")
//    @ResponseBody
//    public ResponseResult<List<Item>> getItemsByOrderId(@PathVariable @Parameter(description = "订单ID") Integer id) {
//        List<Item> items = ordersService.findItemsByOrderId(id);
//        if (items != null) {
//            logger.info("(OrderController)物品查找成功, 订单ID: {}", id);
//            return ResponseResult.success(items);
//        } else {
//            logger.error("(OrderController)未找到物品");
//            return ResponseResult.failure(404, "未找到物品");
//        }
//    }

    @Operation(summary = "更新订单信息")
    @PutMapping("/{id}")
    @ResponseBody
    @CachePut(value = "order", key = "#id")
    public ResponseResult<?> updateOrder(@PathVariable @Parameter(description = "订单ID") Integer id, @RequestBody @Parameter(description = "订单") Orders orders) {
        if (ordersService.updateOrder(id, orders)) {
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
        if (ordersService.deleteOrder(id)) {
            logger.info("(OrderController)订单删除成功, ID: {}", id);
            return ResponseResult.success("订单删除成功");
        } else {
            logger.error("(OrderController)订单删除失败");
            return ResponseResult.failure(404, "订单删除失败");
        }
    }

    @Operation(summary = "删除订单物品")
    @DeleteMapping("/item/{id}")
    @ResponseBody
    public ResponseResult<?> deleteItem(@PathVariable @Parameter(description = "订单ID") Integer id,
                                        @RequestBody @Parameter(description = "物品ID") Integer itemId) {
        if (ordersService.deleteItem(id, itemId)) {
            logger.info("(OrderController)物品删除成功, ID: {}", id);
            return ResponseResult.success("物品删除成功");
        } else {
            logger.error("(OrderController)物品删除失败");
            return ResponseResult.failure(404, "物品删除失败");
        }
    }
}
