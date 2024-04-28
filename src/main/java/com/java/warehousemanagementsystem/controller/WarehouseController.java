package com.java.warehousemanagementsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.warehousemanagementsystem.pojo.Warehouse;
import com.java.warehousemanagementsystem.service.WarehouseService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController
{
    private static final Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    @Resource
    private WarehouseService warehouseService;

    @Operation(summary = "创建新仓库")
    @PostMapping()
    @ResponseBody
    public ResponseResult<?> createWarehouse(
            @RequestParam @Parameter(description = "仓库名称") String name,
            @RequestParam @Parameter(description = "仓库位置") String location,
            @RequestParam @Parameter(description = "管理员") String manager,
            @RequestParam @Parameter(description = "仓库介绍") String description)
    {
        Warehouse warehouse = warehouseService.addWarehouse(name, location, manager, description);
        logger.info("(WarehouseController)仓库创建成功");
        return ResponseResult.success(warehouse);
    }

    @Operation(summary = "更新仓库信息")
    @PutMapping("/{id}")
    @ResponseBody
    @CachePut(value = "warehouse", key = "#id")
    public ResponseResult<?> updateWarehouse(
            @PathVariable @Parameter(description = "仓库id") Integer id,
            @RequestParam(required = false, defaultValue = "") @Parameter(description = "仓库名称") String name,
            @RequestParam(required = false, defaultValue = "") @Parameter(description = "仓库位置") String location,
            @RequestParam(required = false, defaultValue = "") @Parameter(description = "管理员") String manager,
            @RequestParam(required = false, defaultValue = "") @Parameter(description = "仓库介绍") String description)
    {
        Warehouse warehouse = warehouseService.updateWarehouse(id, name, location, manager, description);
        logger.info("(WarehouseController)仓库更新成功");
        return ResponseResult.success(warehouse);
    }

    @Operation(summary = "根据id查找仓库")
    @GetMapping("/{id}")
    @ResponseBody
    @Cacheable(value = "warehouse", key = "#id")
    public ResponseResult<Warehouse> findWarehouseById(
            @PathVariable @Parameter(description = "仓库id") Integer id) {
        Warehouse warehouse = warehouseService.selectWarehouse(id);
        if (warehouse == null) {
            logger.error("(WarehouseController)未找到仓库");
            return ResponseResult.failure(404, "未找到仓库");
        }

        logger.info("(WarehouseController)仓库查找成功");
        return ResponseResult.success(warehouse);
    }

    @Operation(summary = "获取仓库列表")
    @GetMapping()
    @ResponseBody
    @Cacheable(value = "warehouseList")
    public ResponseResult<Page<Warehouse>> getWarehouseList(
            @RequestParam(defaultValue = "") @Parameter(description = "仓库名称") String name,
            @RequestParam(defaultValue = "1") @Parameter(description = "页码") Long pageNo,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页数量") Long pageSize
    ) {
        logger.info("(WarehouseController)获取仓库列表");
        return ResponseResult.success(warehouseService.selectWarehouse(name, pageNo, pageSize));
    }

    @Operation(summary = "删除仓库")
    @DeleteMapping("/{id}")
    @ResponseBody
    @CacheEvict(value = "warehouse", key = "#id")
    public ResponseResult<?> deleteWarehouse(
            @PathVariable @Parameter(description = "仓库id") Integer id) {
        if (!warehouseService.deleteWarehouse(id)) {
            logger.error("(WarehouseController)未找到仓库");
            return ResponseResult.failure(404, "未找到仓库");
        }
        //warehouseService.deleteWarehouse(id);
        logger.info("(WarehouseController)仓库删除成功");
        return ResponseResult.success("仓库删除成功");
    }


}

