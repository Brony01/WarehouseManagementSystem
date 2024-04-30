package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.enums.AppHttpCodeEnum;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.service.ItemService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    void testGetAllItems() {
        List<Item> expectedItems = Arrays.asList(new Item(), new Item());
        given(itemService.findAllItems()).willReturn(expectedItems);

        ResponseResult<List<Item>> result = itemController.getAllItems();

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals(expectedItems.size(), result.getData().size());
    }

    @Test
    void testAddItem() {
        when(itemService.addItem("Test", "Description", 10, 100.0, 1)).thenReturn(true);

        ResponseResult<?> result = itemController.addItem("Test", "Description", 10, 100.0, 1);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    void testGetItemByIdNotFound() {
        when(itemService.findItemById(1)).thenReturn(null);

        ResponseResult<Item> result = itemController.getItemById(1);

        assertNotEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
    }

    @Test
    void testUpdateItem() {
        when(itemService.updateItem(1, "Updated", "New Description", 20, 150.0, 1)).thenReturn(true);

        ResponseResult<?> result = itemController.updateItem(1, "Updated", "New Description", 20, 150.0, 1);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    void testDeleteItem() {
        when(itemService.deleteItem(1)).thenReturn(true);

        ResponseResult<?> result = itemController.deleteItem(1);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());
    }
}
