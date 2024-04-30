package com.java.warehousemanagementsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.warehousemanagementsystem.pojo.Warehouse;
import com.java.warehousemanagementsystem.service.WarehouseService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WarehouseControllerTest {

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private WarehouseController warehouseController;

    @Test
    void testCreateWarehouse() {
        String name = "Main Warehouse";
        String address = "Downtown";
        String manager = "John Doe";
        String description = "Central warehouse";
        Warehouse warehouse = new Warehouse();
        warehouse.setName(name);
        warehouse.setAddress(address);
        warehouse.setManager(manager);
        warehouse.setDescription(description);
        when(warehouseService.addWarehouse(name, address, manager, description)).thenReturn(warehouse);

        ResponseResult<?> result = warehouseController.createWarehouse(name, address, manager, description);

        assertEquals(200, result.getCode());
        assertEquals(warehouse, result.getData());
    }

    @Test
    void testUpdateWarehouse() {
        Integer id = 1;
        String name = "Updated Warehouse";
        String location = "Uptown";
        String manager = "Jane Doe";
        String description = "Updated description";
        Warehouse updatedWarehouse = new Warehouse();
        updatedWarehouse.setId(id);
        updatedWarehouse.setName(name);
        updatedWarehouse.setAddress(location);
        updatedWarehouse.setManager(manager);
        updatedWarehouse.setDescription(description);
        when(warehouseService.updateWarehouse(id, name, location, manager, description)).thenReturn(updatedWarehouse);

        ResponseResult<?> result = warehouseController.updateWarehouse(id, name, location, manager, description);

        assertEquals(200, result.getCode());
        assertEquals(updatedWarehouse, result.getData());
    }

    @Test
    void testFindWarehouseById() {
        Integer id = 1;
        Warehouse warehouse = new Warehouse();
        warehouse.setId(id);
        when(warehouseService.selectWarehouse(id)).thenReturn(warehouse);

        ResponseResult<Warehouse> result = warehouseController.findWarehouseById(id);

        assertEquals(200, result.getCode());
        assertEquals(warehouse, result.getData());
    }

    @Test
    void testFindWarehouseByIdNotFound() {
        Integer id = 99;
        when(warehouseService.selectWarehouse(id)).thenReturn(null);

        ResponseResult<Warehouse> result = warehouseController.findWarehouseById(id);

        assertEquals(404, result.getCode());
        assertNull(result.getData());
    }

    @Test
    void testGetWarehouseList() {
        Page<Warehouse> page = new Page<>();
        when(warehouseService.selectWarehouse("", 1L, 10L)).thenReturn(page);

        ResponseResult<Page<Warehouse>> result = warehouseController.getWarehouseList("", 1L, 10L);

        assertEquals(200, result.getCode());
        assertEquals(page, result.getData());
    }

    @Test
    void testDeleteWarehouse() {
        Integer id = 1;
        when(warehouseService.deleteWarehouse(id)).thenReturn(true);

        ResponseResult<?> result = warehouseController.deleteWarehouse(id);

        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    void testDeleteWarehouseNotFound() {
        Integer id = 99;
        when(warehouseService.deleteWarehouse(id)).thenReturn(false);

        ResponseResult<?> result = warehouseController.deleteWarehouse(id);

        assertEquals(404, result.getCode());
        assertEquals("未找到仓库", result.getMsg());
    }
}
