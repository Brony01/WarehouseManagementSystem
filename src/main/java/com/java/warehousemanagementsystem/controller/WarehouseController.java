package com.java.warehousemanagementsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.warehousemanagementsystem.pojo.Warehouse;
import com.java.warehousemanagementsystem.service.WarehouseService;
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

@Tag(name = "仓库管理", description = "仓库管理的相关操作")
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Operation(summary = "创建新仓库")
    @PostMapping()
    @ApiResponse(responseCode = "200", description = "仓库创建成功")
    @ApiResponse(responseCode = "400", description = "仓库创建失败")
    @ResponseBody
    public Mono<ResponseResult<Warehouse>> createWarehouse(
            @RequestParam @Parameter(description = "仓库名称") String name,
            @RequestParam @Parameter(description = "仓库位置") String location,
            @RequestParam @Parameter(description = "管理员") String manager,
            @RequestParam @Parameter(description = "仓库介绍") String description) {
        return warehouseService.addWarehouse(name, location, manager, description)
                .map(ResponseResult::success);
    }

    @Operation(summary = "更新仓库信息")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "仓库更新成功")
    @ApiResponse(responseCode = "400", description = "仓库更新失败")
    @ResponseBody
    public Mono<ResponseResult<Warehouse>> updateWarehouse(
            @PathVariable Integer id,
            @RequestParam @Parameter(description = "仓库名称") String name,
            @RequestParam @Parameter(description = "仓库位置") String location,
            @RequestParam @Parameter(description = "管理员") String manager,
            @RequestParam @Parameter(description = "仓库介绍") String description) {
        return warehouseService.updateWarehouse(id, name, location, manager, description)
                .map(ResponseResult::success);
    }

    @Operation(summary = "删除仓库")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "仓库删除成功")
    @ApiResponse(responseCode = "400", description = "仓库删除失败")
    @ResponseBody
    public Mono<ResponseResult<Void>> deleteWarehouse(@PathVariable Integer id) {
        return warehouseService.deleteWarehouse(id)
                .thenReturn(ResponseResult.success());
    }

    @Operation(summary = "根据ID查找仓库")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "仓库查找成功")
    @ApiResponse(responseCode = "400", description = "仓库查找失败")
    @ResponseBody
    public Mono<ResponseResult<Warehouse>> getWarehouseById(@PathVariable Integer id) {
        return warehouseService.selectWarehouseById(id)
                .map(ResponseResult::success);
    }

    @Operation(summary = "分页查询仓库")
    @GetMapping()
    @ApiResponse(responseCode = "200", description = "仓库查找成功")
    @ApiResponse(responseCode = "400", description = "仓库查找失败")
    @ResponseBody
    public Flux<Warehouse> getWarehouses(
            @RequestParam @Parameter(description = "仓库名称") String name,
            @RequestParam @Parameter(description = "页码") Long pageNo,
            @RequestParam @Parameter(description = "每页数量") Long pageSize) {
        return warehouseService.selectWarehouse(name, pageNo, pageSize);
    }
}
