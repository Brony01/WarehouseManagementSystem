package com.java.warehousemanagementsystem.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.warehousemanagementsystem.pojo.Warehouse;

public interface WarehouseService {
    Warehouse addWarehouse(String name, String address, String manager, String description);

    boolean deleteWarehouse(Integer id);

    Warehouse updateWarehouse(Integer id, String name, String address, String manager, String description);

    Warehouse selectWarehouse(Integer id);

    Page<Warehouse> selectWarehouse(String name, Long pageNo, Long pageSize);
}
