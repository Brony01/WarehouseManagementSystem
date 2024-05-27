package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import com.java.warehousemanagementsystem.service.OrdersService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "订单管理", description = "订单管理的相关操作")
@RestController
@RequestMapping("/order")
public class OrdersController {
    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Resource
    private OrdersService ordersService;

    @Operation(summary = "添加新订单")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "订单添加成功")
    @ApiResponse(responseCode = "400", description = "订单添加失败")
    public Mono<ResponseResult<?>> addOrder(@RequestParam @Parameter(description = "订单用户名") String username,
                                            @RequestParam @Parameter(description = "订单地址") String address) {
        Orders orders = new Orders();
        orders.setUsername(username);
        orders.setAddress(address);
        return ordersService.addOrder(orders)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(OrderController)订单添加成功, ID: {}", orders.getId());
                        return Mono.just(ResponseResult.success("订单添加成功"));
                    } else {
                        logger.error("(OrderController)订单添加失败");
                        return Mono.just(ResponseResult.failure(400, "订单添加失败"));
                    }
                });
    }

    @Operation(summary = "添加物品")
    @PostMapping("/item/{id}")
    @ApiResponse(responseCode = "200", description = "物品添加成功")
    @ApiResponse(responseCode = "400", description = "物品添加失败")
    public Mono<ResponseResult<?>> addItem(@PathVariable @Parameter(description = "订单ID") Integer id,
                                           @RequestParam @Parameter(description = "物品ID") Integer itemId) {
        return ordersService.addItem(id, itemId)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(OrderController)物品添加成功, ID: {}", id);
                        return Mono.just(ResponseResult.success("物品添加成功"));
                    } else {
                        logger.error("(OrderController)物品添加失败");
                        return Mono.just(ResponseResult.failure(400, "物品添加失败"));
                    }
                });
    }

    @Operation(summary = "获取所有订单")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "成功获取所有订单数据")
    @ApiResponse(responseCode = "404", description = "未找到订单")
    @ApiResponse(responseCode = "400", description = "获取订单失败")
    @Cacheable(value = "allOrders")
    public Mono<ResponseResult<List<Orders>>> getAllOrders() {
        return ordersService.findAllOrders()
                .collectList()
                .flatMap(orders -> {
                    logger.info("(OrderController)获取订单列表, size = {}", orders.size());
                    return Mono.just(ResponseResult.success(orders));
                });
    }

    @Operation(summary = "根据ID获取订单信息及其物品")
    @GetMapping("/{id}")
    @Cacheable(value = "order", key = "#id")
    public Mono<ResponseResult<Map<String, Object>>> getOrderById(@PathVariable @Parameter(description = "订单ID") Integer id) {
        return ordersService.findOrderById(id)
                .flatMap(order -> ordersService.findItemsByOrderId(id)
                        .collectList()
                        .flatMap(items -> {
                            logger.info("(OrderController)订单查找成功, ID: {}", id);
                            Map<String, Object> result = new HashMap<>();
                            result.put("order", order);
                            result.put("items", items);
                            return Mono.just(ResponseResult.success(result));
                        })
                )
                .switchIfEmpty(Mono.defer(() -> {
                    logger.error("(OrderController)未找到订单, ID: {}", id);
                    return Mono.just(ResponseResult.failure(404, "未找到订单"));
                }));
    }

    @Operation(summary = "根据用户名获取订单信息")
    @GetMapping("/user")
    @ApiResponse(responseCode = "200", description = "成功获取所有订单数据")
    @ApiResponse(responseCode = "404", description = "未找到订单")
    @ApiResponse(responseCode = "400", description = "获取订单失败")
    public Mono<ResponseResult<List<Orders>>> getOrdersByUserId(@RequestParam @Parameter(description = "用户名") String username) {
        return ordersService.findOrdersByUsername(username)
                .collectList()
                .flatMap(orders -> {
                    if (!orders.isEmpty()) {
                        logger.info("(OrderController)订单查找成功, 用户ID: {}", username);
                        return Mono.just(ResponseResult.success(orders));
                    } else {
                        logger.error("(OrderController)未找到订单");
                        return Mono.just(ResponseResult.failure(404, "未找到订单"));
                    }
                });
    }

    @Operation(summary = "根据订单状态获取订单信息")
    @GetMapping("/status/{status}")
    @ApiResponse(responseCode = "200", description = "成功获取所有订单数据")
    @ApiResponse(responseCode = "404", description = "未找到订单")
    @ApiResponse(responseCode = "400", description = "获取订单失败")
    public Mono<ResponseResult<List<Orders>>> getOrdersByStatus(@PathVariable @Parameter(description = "订单状态") String status) {
        return ordersService.findOrdersByStatus(status)
                .collectList()
                .flatMap(orders -> {
                    if (!orders.isEmpty()) {
                        logger.info("(OrderController)订单查找成功, 订单状态: {}", status);
                        return Mono.just(ResponseResult.success(orders));
                    } else {
                        logger.error("(OrderController)未找到订单");
                        return Mono.just(ResponseResult.failure(404, "未找到订单"));
                    }
                });
    }

    @Operation(summary = "根据地址获取订单信息")
    @GetMapping("/address")
    @ApiResponse(responseCode = "200", description = "成功获取所有订单数据")
    @ApiResponse(responseCode = "404", description = "未找到订单")
    @ApiResponse(responseCode = "400", description = "获取订单失败")
    public Mono<ResponseResult<List<Orders>>> getOrdersByAddress(@RequestParam @Parameter(description = "地址") String address) {
        return ordersService.findOrdersByAddress(address)
                .collectList()
                .flatMap(orders -> {
                    if (!orders.isEmpty()) {
                        logger.info("(OrderController)订单查找成功, 地址: {}", address);
                        return Mono.just(ResponseResult.success(orders));
                    } else {
                        logger.error("(OrderController)未找到订单");
                        return Mono.just(ResponseResult.failure(404, "未找到订单"));
                    }
                });
    }

    @Operation(summary = "更新订单信息")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "订单信息更新成功")
    @ApiResponse(responseCode = "400", description = "订单信息更新失败")
    @CachePut(value = "order", key = "#id")
    public Mono<ResponseResult<?>> updateOrder(@PathVariable @Parameter(description = "订单ID") Integer id,
                                               @RequestBody @Parameter(description = "订单") Orders orders) {
        return ordersService.updateOrder(id, orders)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(OrderController)订单信息更新成功, ID: {}", id);
                        return Mono.just(ResponseResult.success("订单信息更新成功"));
                    } else {
                        logger.error("(OrderController)订单信息更新失败");
                        return Mono.just(ResponseResult.failure(400, "订单信息更新失败"));
                    }
                });
    }

    @Operation(summary = "删除订单")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "订单删除成功")
    @ApiResponse(responseCode = "404", description = "订单删除失败")
    @ApiResponse(responseCode = "400", description = "订单删除失败")
    @CacheEvict(value = "order", key = "#id")
    public Mono<ResponseResult<?>> deleteOrder(@PathVariable @Parameter(description = "订单ID") Integer id) {
        return ordersService.deleteOrder(id)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(OrderController)订单删除成功, ID: {}", id);
                        return Mono.just(ResponseResult.success("订单删除成功"));
                    } else {
                        logger.error("(OrderController)订单删除失败");
                        return Mono.just(ResponseResult.failure(404, "订单删除失败"));
                    }
                });
    }

    @Operation(summary = "删除订单物品")
    @DeleteMapping("/item/{id}")
    @ApiResponse(responseCode = "200", description = "物品删除成功")
    @ApiResponse(responseCode = "404", description = "物品删除失败")
    @ApiResponse(responseCode = "400", description = "物品删除失败")
    public Mono<ResponseResult<?>> deleteItem(@PathVariable @Parameter(description = "订单ID") Integer id,
                                              @RequestParam @Parameter(description = "物品ID") Integer itemId) {
        return ordersService.deleteItem(id, itemId)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(OrderController)物品删除成功, ID: {}", id);
                        return Mono.just(ResponseResult.success("物品删除成功"));
                    } else {
                        logger.error("(OrderController)物品删除失败");
                        return Mono.just(ResponseResult.failure(404, "物品删除失败"));
                    }
                });
    }
}
