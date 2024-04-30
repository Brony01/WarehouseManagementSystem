package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.service.ItemService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Resource
    private ItemService itemService;

    @Operation(summary = "获取物品列表")
    @GetMapping()
    @ResponseBody
    @Cacheable(value = "itemList")
    public ResponseResult<List<Item>> getAllItems() {
        List<Item> items = itemService.findAllItems();
        logger.info("(ItemController)获取物品列表, size = {}, items = {}", items.size(), items);
        return ResponseResult.success(items);
    }

    @Operation(summary = "添加新物品")
    @PostMapping()
    @ResponseBody
    public ResponseResult<?> addItem(
            @RequestParam @Parameter(description = "物品名称") String name,
            @RequestParam @Parameter(description = "物品描述") String description,
            @RequestParam @Parameter(description = "物品数量") Integer quantity,
            @RequestParam @Parameter(description = "物品价格") Double price,
            @RequestParam @Parameter(description = "仓库ID") Integer warehouseId) {
        if (itemService.addItem(name, description, quantity, price, warehouseId)) {
            logger.info("(ItemController)物品添加成功");
            return ResponseResult.success("物品添加成功");
        } else {
            logger.error("(ItemController)物品添加失败");
            return ResponseResult.failure(400, "物品添加失败");
        }
    }

    @Operation(summary = "根据ID获取物品信息")
    @GetMapping("/{id}")
    @ResponseBody
    @Cacheable(value = "item", key = "#id")
    public ResponseResult<Item> getItemById(
            @PathVariable @Parameter(description = "物品ID") Integer id) {
        Item item = itemService.findItemById(id);
        if (item == null) {
            logger.error("(ItemController)未找到物品");
            return ResponseResult.failure(404, "未找到物品");
        }

        logger.info("(ItemController)物品查找成功");
        return ResponseResult.success(item);
    }

    @Operation(summary = "更新物品信息")
    @PutMapping("/{id}")
    @ResponseBody
    @CachePut(value = "item", key = "#id")
    public ResponseResult<?> updateItem(
            @PathVariable @Parameter(description = "物品ID") Integer id,
            @RequestParam @Parameter(description = "物品名称") String name,
            @RequestParam @Parameter(description = "物品描述") String description,
            @RequestParam @Parameter(description = "物品数量") Integer quantity,
            @RequestParam @Parameter(description = "物品价格") Double price,
            @RequestParam @Parameter(description = "仓库ID") Integer warehouseId) {
        if (itemService.updateItem(id, name, description, quantity, price, warehouseId)) {
            logger.info("(ItemController)物品信息更新成功");
            return ResponseResult.success("物品信息更新成功");
        } else {
            logger.error("(ItemController)物品信息更新失败");
            return ResponseResult.failure(400, "物品信息更新失败");
        }
    }

    @Operation(summary = "删除物品")
    @DeleteMapping("/{id}")
    @ResponseBody
    @CacheEvict(value = "item", key = "#id")
    public ResponseResult<?> deleteItem(
            @PathVariable @Parameter(description = "物品ID") Integer id) {
        if (itemService.deleteItem(id)) {
            logger.info("(ItemController)物品删除成功");
            return ResponseResult.success("物品删除成功");
        } else {
            logger.error("(ItemController)物品删除失败");
            return ResponseResult.failure(404, "物品删除失败");
        }
    }
}
