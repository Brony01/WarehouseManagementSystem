package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.Warehouse;
import com.java.warehousemanagementsystem.service.WarehouseService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "仓库管理", description = "仓库管理的相关操作")
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    @Resource
    private WarehouseService warehouseService;

    @Operation(summary = "创建新仓库")
    @PostMapping()
    @ApiResponse(responseCode = "200", description = "仓库创建成功")
    @ApiResponse(responseCode = "400", description = "仓库创建失败")
    public Mono<ResponseResult<Warehouse>> createWarehouse(
            @RequestParam @Parameter(description = "仓库名称") String name,
            @RequestParam @Parameter(description = "仓库位置") String location,
            @RequestParam @Parameter(description = "管理员") String manager,
            @RequestParam @Parameter(description = "仓库介绍") String description) {
        return warehouseService.addWarehouse(name, location, manager, description)
                .flatMap(warehouse -> {
                    logger.info("(WarehouseController)仓库创建成功");
                    return Mono.just(ResponseResult.success(warehouse));
                })
                .onErrorResume(e -> {
                    logger.error("(WarehouseController)仓库创建失败: {}", e.getMessage());
                    return Mono.just(ResponseResult.failure(400, "仓库创建失败"));
                });
    }

    @Operation(summary = "更新仓库信息")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "仓库更新成功")
    @ApiResponse(responseCode = "400", description = "仓库更新失败")
    @CachePut(value = "warehouse", key = "#id")
    public Mono<ResponseResult<Warehouse>> updateWarehouse(
            @PathVariable @Parameter(description = "仓库id") Integer id,
            @RequestParam(required = false, defaultValue = "") @Parameter(description = "仓库名称") String name,
            @RequestParam(required = false, defaultValue = "") @Parameter(description = "仓库位置") String location,
            @RequestParam(required = false, defaultValue = "") @Parameter(description = "管理员") String manager,
            @RequestParam(required = false, defaultValue = "") @Parameter(description = "仓库介绍") String description) {
        return warehouseService.updateWarehouse(id, name, location, manager, description)
                .flatMap(warehouse -> {
                    logger.info("(WarehouseController)仓库更新成功");
                    return Mono.just(ResponseResult.success(warehouse));
                })
                .onErrorResume(e -> {
                    logger.error("(WarehouseController)仓库更新失败: {}", e.getMessage());
                    return Mono.just(ResponseResult.failure(400, "仓库更新失败"));
                });
    }

    @Operation(summary = "根据id查找仓库")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "查找成功")
    @ApiResponse(responseCode = "404", description = "未找到仓库")
    @ApiResponse(responseCode = "400", description = "查找失败")
    @Cacheable(value = "warehouse", key = "#id")
    public Mono<ResponseResult<Warehouse>> findWarehouseById(
            @PathVariable @Parameter(description = "仓库id") Integer id) {
        return warehouseService.selectWarehouse(id)
                .flatMap(warehouse -> {
                    logger.info("(WarehouseController)仓库查找成功");
                    return Mono.just(ResponseResult.success(warehouse));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.error("(WarehouseController)未找到仓库");
                    return Mono.just(ResponseResult.failure(404, "未找到仓库"));
                }));
    }

    @Operation(summary = "获取仓库列表")
    @GetMapping()
    @ApiResponse(responseCode = "200", description = "成功获取仓库列表")
    @ApiResponse(responseCode = "404", description = "未找到仓库")
    @ApiResponse(responseCode = "400", description = "获取仓库列表失败")
    @Cacheable(value = "warehouseList")
    public Mono<ResponseResult<PageImpl<Warehouse>>> getWarehouseList(
            @RequestParam(defaultValue = "") @Parameter(description = "仓库名称") String name,
            @RequestParam(defaultValue = "1") @Parameter(description = "页码") Long pageNo,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页数量") Long pageSize) {
        logger.info("(WarehouseController)获取仓库列表");
        return warehouseService.selectWarehouse(name, pageNo, pageSize)
                .flatMap(page -> Mono.just(ResponseResult.success(page)))
                .onErrorResume(e -> {
                    logger.error("(WarehouseController)获取仓库列表失败: {}", e.getMessage());
                    return Mono.just(ResponseResult.failure(400, "获取仓库列表失败"));
                });
    }

    @Operation(summary = "删除仓库")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "仓库删除成功")
    @ApiResponse(responseCode = "404", description = "未找到仓库")
    @ApiResponse(responseCode = "400", description = "仓库删除失败")
    @CacheEvict(value = "warehouse", key = "#id")
    public Mono<ResponseResult<?>> deleteWarehouse(
            @PathVariable @Parameter(description = "仓库id") Integer id) {
        return warehouseService.deleteWarehouse(id)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(WarehouseController)仓库删除成功");
                        return Mono.just(ResponseResult.success("仓库删除成功"));
                    } else {
                        logger.error("(WarehouseController)未找到仓库");
                        return Mono.just(ResponseResult.failure(404, "未找到仓库"));
                    }
                });
    }
}
