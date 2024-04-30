package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.warehousemanagementsystem.mapper.WarehouseMapper;
import com.java.warehousemanagementsystem.pojo.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceImplTest {

    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addWarehouseTest() {
        Warehouse warehouse = new Warehouse(null, "Warehouse1", "Address1", "Manager1", "Description1", new Date());
        when(warehouseMapper.insert(any(Warehouse.class))).thenReturn(1);

        Warehouse created = warehouseService.addWarehouse("Warehouse1", "Address1", "Manager1", "Description1");

        assertNotNull(created);
        assertEquals("Warehouse1", created.getName());
        verify(warehouseMapper).insert(any(Warehouse.class));
    }

    @Test
    void deleteWarehouseTest_success() {
        // Arrange
        int id = 1;
        when(warehouseMapper.deleteById(id)).thenReturn(1); // Assume '1' signifies successful deletion of one record

        // Act
        boolean result = warehouseService.deleteWarehouse(id);

        // Assert
        assertTrue(result);
        verify(warehouseMapper).deleteById(id);
    }


    @Test
    void deleteWarehouseTest_failure_nullId() {
        // Arrange
        Integer id = null;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.deleteWarehouse(id);
        });

        assertEquals("仓库id不能为空", exception.getMessage());
        verify(warehouseMapper, never()).deleteById(any());
    }

    @Test
    void updateWarehouseTest() {
        Warehouse existing = new Warehouse(1, "OldName", "OldAddress", "OldManager", "OldDescription", new Date());
        when(warehouseMapper.selectById(1)).thenReturn(existing);
        when(warehouseMapper.updateById(any(Warehouse.class))).thenReturn(1);

        Warehouse updated = warehouseService.updateWarehouse(1, "NewName", "NewAddress", "NewManager", "NewDescription");

        assertNotNull(updated);
        assertEquals("NewName", updated.getName());
        verify(warehouseMapper).updateById(updated);
    }

    @Test
    void selectWarehouseTest() {
        Warehouse warehouse = new Warehouse(1, "Warehouse1", "Address1", "Manager1", "Description1", new Date());
        when(warehouseMapper.selectById(1)).thenReturn(warehouse);

        Warehouse found = warehouseService.selectWarehouse(1);

        assertNotNull(found);
        assertEquals("Warehouse1", found.getName());
        verify(warehouseMapper).selectById(1);
    }

    @Test
    void selectWarehousePageTest() {
        Page<Warehouse> page = new Page<>(1L, 10L);
        when(warehouseMapper.selectPage(any(Page.class), any())).thenReturn(page);

        Page<Warehouse> result = warehouseService.selectWarehouse("Warehouse1", 1L, 10L);

        assertNotNull(result);
        verify(warehouseMapper).selectPage(any(Page.class), any());
    }
}
