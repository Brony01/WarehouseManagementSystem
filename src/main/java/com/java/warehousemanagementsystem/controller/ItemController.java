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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "物品管理", description = "物品管理的相关操作")
@RestController
@RequestMapping("/item")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "获取物品列表")
    @GetMapping()
    @ApiResponse(responseCode = "200", description = "成功获取物品列表")
    @ApiResponse(responseCode = "404", description = "未找到物品")
    @ApiResponse(responseCode = "400", description = "获取物品列表失败")
    @ResponseBody
    @Cacheable(value = "itemList")
    public Flux<Item> getAllItems() {
        return itemService.findAllItems();
    }

    @Operation(summary = "添加新物品")
    @PostMapping()
    @ApiResponse(responseCode = "200", description = "物品添加成功")
    @ApiResponse(responseCode = "400", description = "物品添加失败")
    @ResponseBody
    public Mono<ResponseResult<String>> addItem(
            @RequestParam @Parameter(description = "物品名称") String name,
            @RequestParam @Parameter(description = "物品描述") String description,
            @RequestParam @Parameter(description = "物品数量") Integer quantity,
            @RequestParam @Parameter(description = "物品价格") Double price,
            @RequestParam @Parameter(description = "仓库ID") Integer warehouseId) {
        return itemService.addItem(name, description, quantity, price, warehouseId)
                .map(success -> success ? ResponseResult.success("物品添加成功") : ResponseResult.failure(400, "物品添加失败"));
    }

    @Operation(summary = "更新物品信息")
    @PutMapping()
    @ApiResponse(responseCode = "200", description = "物品信息更新成功")
    @ApiResponse(responseCode = "400", description = "物品信息更新失败")
    @ResponseBody
    public Mono<ResponseResult<String>> updateItem(
            @RequestParam @Parameter(description = "物品ID") Integer id,
            @RequestParam @Parameter(description = "物品名称") String name,
            @RequestParam @Parameter(description = "物品描述") String description,
            @RequestParam @Parameter(description = "物品数量") Integer quantity,
            @RequestParam @Parameter(description = "物品价格") Double price,
            @RequestParam @Parameter(description = "仓库ID") Integer warehouseId) {
        return itemService.updateItem(id, name, description, quantity, price, warehouseId)
                .map(success -> success ? ResponseResult.success("物品信息更新成功") : ResponseResult.failure(400, "物品信息更新失败"));
    }

    @Operation(summary = "根据ID查找物品")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "成功找到物品")
    @ApiResponse(responseCode = "404", description = "未找到物品")
    @ApiResponse(responseCode = "400", description = "查找物品失败")
    @ResponseBody
    public Mono<ResponseResult<Item>> getItemById(@PathVariable Integer id) {
        return itemService.findItemById(id)
                .map(ResponseResult::success);
    }

    @Operation(summary = "删除物品")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "成功删除物品")
    @ApiResponse(responseCode = "400", description = "删除物品失败")
    @ResponseBody
    @CacheEvict(value = "itemList", allEntries = true)
    public Mono<ResponseResult<String>> deleteItem(@PathVariable Integer id) {
        return itemService.deleteItem(id)
                .map(success -> success ? ResponseResult.success("成功删除物品") : ResponseResult.failure(400, "删除物品失败"));
    }
}
