package com.java.warehousemanagementsystem.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.mapper.ItemMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void testAddItem() {
        String name = "Item1";
        String description = "A new item";
        Integer quantity = 10;
        Double price = 20.0;
        Integer warehouseId = 1;
        when(itemMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);

        assertTrue(itemService.addItem(name, description, quantity, price, warehouseId));
    }

    @Test
    void testAddItemWithDuplicate() {
        String name = "Item1";
        String description = "A new item";
        Integer quantity = 10;
        Double price = 20.0;
        Integer warehouseId = 1;
        when(itemMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        assertThrows(IllegalArgumentException.class, () -> itemService.addItem(name, description, quantity, price, warehouseId));
    }


    @Test
    void testUpdateItem() {
        Integer id = 1;
        String name = "Updated Item";
        String description = "Updated description";
        Integer quantity = 15;
        Double price = 25.0;
        Integer warehouseId = 1;
        Item item = new Item();
        item.setId(id);
        when(itemMapper.selectById(id)).thenReturn(item);

        assertTrue(itemService.updateItem(id, name, description, quantity, price, warehouseId));
        verify(itemMapper).updateById(item);
    }

    @Test
    void testUpdateItemNotFound() {
        Integer id = 99;
        when(itemMapper.selectById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(id, "name", "description", 10, 20.0, 1));
    }

    @Test
    void testFindItemById() {
        Integer id = 1;
        Item item = new Item();
        item.setId(id);
        when(itemMapper.selectById(id)).thenReturn(item);

        Item result = itemService.findItemById(id);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindItemByIdWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> itemService.findItemById(null));
    }

    @Test
    void testDeleteItem() {
        Integer id = 1;
        when(itemMapper.deleteById(id)).thenReturn(1);

        assertTrue(itemService.deleteItem(id));
    }

    @Test
    void testDeleteItemNotFound() {
        Integer id = 99;
        when(itemMapper.deleteById(id)).thenReturn(0);

        assertFalse(itemService.deleteItem(id));
    }

    @Test
    void testFindAllItems() {
        List<Item> expectedItems = new ArrayList<>();
        expectedItems.add(new Item());
        expectedItems.add(new Item());
        when(itemMapper.selectList(null)).thenReturn(expectedItems);

        List<Item> result = itemService.findAllItems();
        assertEquals(2, result.size());
    }

    @Test
    void testAddItemWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> itemService.addItem(null, "description", 10, 20.0, 1), "物品名称不能为空");
        assertThrows(IllegalArgumentException.class, () -> itemService.addItem("Item", "", 10, 20.0, 1), "物品描述不能为空");
        assertThrows(IllegalArgumentException.class, () -> itemService.addItem("Item", "description", -1, 20.0, 1), "物品数量不正确");
        assertThrows(IllegalArgumentException.class, () -> itemService.addItem("Item", "description", 10, -20.0, 1), "物品价格不正确");
    }

    @Test
    void testUpdateItemWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(1, null, "description", 10, 20.0, 1), "物品名称不能为空");
        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(1, "Item", "", 10, 20.0, 1), "物品描述不能为空");
        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(1, "Item", "description", -1, 20.0, 1), "物品数量不正确");
        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(1, "Item", "description", 10, -20.0, 1), "物品价格不正确");
    }

    @Test
    void testFindItemByIdWithInvalidId() {
        Integer invalidId = null;
        assertThrows(IllegalArgumentException.class, () -> itemService.findItemById(invalidId), "物品ID不能为空");
    }

    @Test
    void testDeleteItemWithInvalidId() {
        Integer invalidId = null;
        assertThrows(IllegalArgumentException.class, () -> itemService.deleteItem(invalidId), "物品ID不能为空");
    }
}
