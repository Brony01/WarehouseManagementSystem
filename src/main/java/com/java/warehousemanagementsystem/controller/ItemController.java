package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.service.ItemService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "物品管理", description = "物品管理的相关操作")
@Controller
@RequestMapping("/item")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Operation(summary = "获取物品列表")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "成功获取物品列表")
    @ApiResponse(responseCode = "404", description = "未找到物品")
    @ApiResponse(responseCode = "400", description = "获取物品列表失败")
    @ResponseBody
    @Cacheable(value = "itemList")
    public Mono<ResponseResult<Flux<Item>>> getAllItems() {
        Flux<Item> items = itemService.findAllItems();
        return items.collectList()
                .flatMap(itemList -> {
                    logger.info("(ItemController)获取物品列表, size = {}, items = {}", itemList.size(), itemList);
                    return Mono.just(ResponseResult.success(items));
                });
    }

    @Operation(summary = "添加新物品")
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "物品添加成功")
    @ApiResponse(responseCode = "400", description = "物品添加失败")
    @ResponseBody
    public Mono<ResponseResult<?>> addItem(
            @RequestParam @Parameter(description = "物品名称") String name,
            @RequestParam @Parameter(description = "物品描述") String description,
            @RequestParam @Parameter(description = "物品数量") Integer quantity,
            @RequestParam @Parameter(description = "物品价格") Double price,
            @RequestParam @Parameter(description = "仓库ID") Integer warehouseId) {
        return itemService.addItem(name, description, quantity, price, warehouseId)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(ItemController)物品添加成功");
                        return Mono.just(ResponseResult.success("物品添加成功"));
                    } else {
                        logger.error("(ItemController)物品添加失败");
                        return Mono.just(ResponseResult.failure(400, "物品添加失败"));
                    }
                });
    }

    @Operation(summary = "根据ID获取物品信息")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "成功获取物品信息")
    @ApiResponse(responseCode = "404", description = "未找到物品")
    @ApiResponse(responseCode = "400", description = "获取物品信息失败")
    @ResponseBody
    @Cacheable(value = "item", key = "#id")
    public Mono<ResponseResult<Item>> getItemById(
            @PathVariable @Parameter(description = "物品ID") Integer id) {
        return itemService.findItemById(id)
                .flatMap(item -> {
                    logger.info("(ItemController)物品查找成功");
                    return Mono.just(ResponseResult.success(item));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.error("(ItemController)未找到物品");
                    return Mono.just(ResponseResult.failure(404, "未找到物品"));
                }));
    }

    @Operation(summary = "更新物品信息")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "物品信息更新成功")
    @ApiResponse(responseCode = "400", description = "物品信息更新失败")
    @ResponseBody
    @CachePut(value = "item", key = "#id")
    public Mono<ResponseResult<?>> updateItem(
            @PathVariable @Parameter(description = "物品ID") Integer id,
            @RequestParam @Parameter(description = "物品名称") String name,
            @RequestParam @Parameter(description = "物品描述") String description,
            @RequestParam @Parameter(description = "物品数量") Integer quantity,
            @RequestParam @Parameter(description = "物品价格") Double price,
            @RequestParam @Parameter(description = "仓库ID") Integer warehouseId) {
        return itemService.updateItem(id, name, description, quantity, price, warehouseId)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(ItemController)物品信息更新成功");
                        return Mono.just(ResponseResult.success("物品信息更新成功"));
                    } else {
                        logger.error("(ItemController)物品信息更新失败");
                        return Mono.just(ResponseResult.failure(400, "物品信息更新失败"));
                    }
                });
    }

    @Operation(summary = "删除物品")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "物品删除成功")
    @ApiResponse(responseCode = "404", description = "物品删除失败")
    @ApiResponse(responseCode = "400", description = "物品删除失败")
    @ResponseBody
    @CacheEvict(value = "item", key = "#id")
    public Mono<ResponseResult<?>> deleteItem(
            @PathVariable @Parameter(description = "物品ID") Integer id) {
        return itemService.deleteItem(id)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(ItemController)物品删除成功");
                        return Mono.just(ResponseResult.success("物品删除成功"));
                    } else {
                        logger.error("(ItemController)物品删除失败");
                        return Mono.just(ResponseResult.failure(404, "物品删除失败"));
                    }
                });
    }
}
