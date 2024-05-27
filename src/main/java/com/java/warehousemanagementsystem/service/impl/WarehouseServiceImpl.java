package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.warehousemanagementsystem.mapper.WarehouseMapper;
import com.java.warehousemanagementsystem.pojo.Warehouse;
import com.java.warehousemanagementsystem.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public Mono<Warehouse> addWarehouse(String name, String location, String manager, String description) {
        return Mono.fromCallable(() -> {
            Warehouse warehouse = new Warehouse();
            warehouse.setName(name);
            warehouse.setAddress(location);
            warehouse.setManager(manager);
            warehouse.setDescription(description);
            warehouseMapper.insert(warehouse);
            return warehouse;
        });
    }

    @Override
    public Mono<Warehouse> updateWarehouse(Integer id, String name, String location, String manager, String description) {
        return Mono.fromCallable(() -> {
            Warehouse warehouse = warehouseMapper.selectById(id);
            if (warehouse != null) {
                warehouse.setName(name);
                warehouse.setAddress(location);
                warehouse.setManager(manager);
                warehouse.setDescription(description);
                warehouseMapper.updateById(warehouse);
            }
            return warehouse;
        });
    }

    @Override
    public Mono<Void> deleteWarehouse(Integer id) {
        return Mono.fromRunnable(() -> warehouseMapper.deleteById(id));
    }

    @Override
    public Mono<Warehouse> selectWarehouseById(Integer id) {
        return Mono.fromCallable(() -> warehouseMapper.selectById(id));
    }

    @Override
    public Flux<Warehouse> selectWarehouse(String name, Long pageNo, Long pageSize) {
        return Flux.defer(() -> {
            QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", name);
            Page<Warehouse> page = new Page<>(pageNo, pageSize);
            return Flux.fromIterable(warehouseMapper.selectPage(page, queryWrapper).getRecords());
        });
    }
}
