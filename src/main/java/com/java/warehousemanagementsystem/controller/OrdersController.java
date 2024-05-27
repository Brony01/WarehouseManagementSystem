package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import com.java.warehousemanagementsystem.service.OrdersService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "订单管理", description = "订单管理的相关操作")
@RestController
@RequestMapping("/order")
public class OrdersController {
    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Operation(summary = "添加新订单")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "订单添加成功")
    @ApiResponse(responseCode = "400", description = "订单添加失败")
    @ResponseBody
    public Mono<ResponseResult<String>> addOrder(
            @RequestParam @Parameter(description = "用户名") String username,
            @RequestParam @Parameter(description = "地址") String address) {
        Orders orders = new Orders();
        orders.setUsername(username);
        orders.setAddress(address);
        return ordersService.addOrder(orders)
                .map(success -> success ? ResponseResult.success("订单添加成功") : ResponseResult.failure(400, "订单添加失败"));
    }

    @Operation(summary = "更新订单")
    @PutMapping
    @ApiResponse(responseCode = "200", description = "订单更新成功")
    @ApiResponse(responseCode = "400", description = "订单更新失败")
    @ResponseBody
    public Mono<ResponseResult<String>> updateOrder(@RequestBody Orders orders) {
        return ordersService.updateOrder(orders)
                .map(success -> success ? ResponseResult.success("订单更新成功") : ResponseResult.failure(400, "订单更新失败"));
    }

    @Operation(summary = "根据ID查找订单")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "订单查找成功")
    @ApiResponse(responseCode = "400", description = "订单查找失败")
    @ResponseBody
    public Mono<ResponseResult<Orders>> getOrderById(@PathVariable Integer id) {
        return ordersService.findOrderById(id)
                .map(ResponseResult::success);
    }

    @Operation(summary = "根据状态查找订单")
    @GetMapping("/status")
    @ApiResponse(responseCode = "200", description = "订单查找成功")
    @ApiResponse(responseCode = "400", description = "订单查找失败")
    @ResponseBody
    public Flux<Orders> getOrdersByStatus(@RequestParam @Parameter(description = "订单状态") String status) {
        return ordersService.findOrdersByStatus(status);
    }

    @Operation(summary = "根据地址查找订单")
    @GetMapping("/address")
    @ApiResponse(responseCode = "200", description = "订单查找成功")
    @ApiResponse(responseCode = "400", description = "订单查找失败")
    @ResponseBody
    public Flux<Orders> getOrdersByAddress(@RequestParam @Parameter(description = "订单地址") String address) {
        return ordersService.findOrdersByAddress(address);
    }

    @Operation(summary = "获取订单中的商品")
    @GetMapping("/{id}/items")
    @ApiResponse(responseCode = "200", description = "订单商品查找成功")
    @ApiResponse(responseCode = "400", description = "订单商品查找失败")
    @ResponseBody
    public Flux<Item> getItemsByOrderId(@PathVariable Integer id) {
        return ordersService.findItemsByOrderId(id);
    }
}
