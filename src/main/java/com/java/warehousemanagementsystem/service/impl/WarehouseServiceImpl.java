package com.java.warehousemanagementsystem.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.warehousemanagementsystem.mapper.WarehouseMapper;
import com.java.warehousemanagementsystem.pojo.Warehouse;
import com.java.warehousemanagementsystem.service.WarehouseService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseServiceImpl.class);
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");

    @Resource
    WarehouseMapper warehouseMapper;

    @Override
    public Warehouse addWarehouse(String name, String address, String manager, String description) {
        Date createTime = new Date();
        Warehouse warehouse = new Warehouse(null, name, address, manager, description, createTime);
        warehouseMapper.insert(warehouse);
        logger.info("(WarehouseServiceImpl)仓库添加成功");

        return warehouse;
    }

    @Override
    public boolean deleteWarehouse(Integer id) {
        if (id == null) {
            logger.error("(WarehouseServiceImpl)仓库id不能为空");
            throw new IllegalArgumentException("仓库id不能为空");
        }
        warehouseMapper.deleteById(id);
        logger.info("(WarehouseServiceImpl)仓库删除成功");
        return true;
    }

    @Override
    public Warehouse updateWarehouse(Integer id, String name, String address, String manager, String description) {
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
        Warehouse warehouse = warehouseMapper.selectById(id);

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
//        warehouse.setUpdateTime(updateTime);

        warehouseMapper.updateById(warehouse);
        logger.info("(WarehouseServiceImpl)仓库更新成功");
        return warehouse;
    }

    @Override
    public Warehouse selectWarehouse(Integer id) {
        if (id == null) {
            logger.error("(WarehouseServiceImpl)仓库id不能为空");
            throw new IllegalArgumentException("仓库id不能为空");
        }

        Warehouse warehouse = warehouseMapper.selectById(id);

        logger.info("(WarehouseServiceImpl)仓库查找成功，仓库信息：" + warehouse);
        return warehouse;
    }

    @Override
    public Page<Warehouse> selectWarehouse(String name, Long pageNo, Long pageSize) {

        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        Page<Warehouse> page = new Page<>(pageNo, pageSize);
        return warehouseMapper.selectPage(page, queryWrapper);
    }
}
