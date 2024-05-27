package com.java.warehousemanagementsystem.service.impl;


import com.java.warehousemanagementsystem.pojo.Warehouse;
import com.java.warehousemanagementsystem.repository.WarehouseRepository;
import com.java.warehousemanagementsystem.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseServiceImpl.class);
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    public Mono<Warehouse> addWarehouse(String name, String address, String manager, String description) {
        Date createTime = new Date();
        Warehouse warehouse = new Warehouse(null, name, address, manager, description, createTime);
        return warehouseRepository.save(warehouse)
                .doOnSuccess(savedWarehouse -> logger.info("(WarehouseServiceImpl)仓库添加成功"));
    }

    @Override
    public Mono<Boolean> deleteWarehouse(Integer id) {
        if (id == null) {
            logger.error("(WarehouseServiceImpl)仓库id不能为空");
            return Mono.error(new IllegalArgumentException("仓库id不能为空"));
        }
        return warehouseRepository.deleteById(id)
                .thenReturn(true)
                .doOnSuccess(success -> logger.info("(WarehouseServiceImpl)仓库删除成功"));
    }

    @Override
    public Mono<Warehouse> updateWarehouse(Integer id, String name, String address, String manager, String description) {
        return warehouseRepository.findById(id)
                .flatMap(warehouse -> {
                    if (!name.isEmpty()) {
                        warehouse.setName(name);
                    }
                    if (!address.isEmpty()) {
                        warehouse.setAddress(address);
                    }
                    if (!manager.isEmpty()) {
                        warehouse.setManager(manager);
                    }
                    if (!description.isEmpty()) {
                        warehouse.setDescription(description);
                    }
                    return warehouseRepository.save(warehouse)
                            .doOnSuccess(updatedWarehouse -> logger.info("(WarehouseServiceImpl)仓库更新成功"));
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("仓库不存在")));
    }

    @Override
    public Mono<Warehouse> selectWarehouse(Integer id) {
        if (id == null) {
            logger.error("(WarehouseServiceImpl)仓库id不能为空");
            return Mono.error(new IllegalArgumentException("仓库id不能为空"));
        }
        return warehouseRepository.findById(id)
                .doOnSuccess(warehouse -> logger.info("(WarehouseServiceImpl)仓库查找成功，仓库信息：" + warehouse));
    }

    @Override
    public Mono<PageImpl<Warehouse>> selectWarehouse(String name, Long pageNo, Long pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo.intValue(), pageSize.intValue());
        return warehouseRepository.findByNameContaining(name, pageRequest)
                .collectList()
                .zipWith(warehouseRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
    }
}
