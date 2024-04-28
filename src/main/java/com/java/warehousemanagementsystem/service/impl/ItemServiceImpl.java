package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.mapper.ItemMapper;
import com.java.warehousemanagementsystem.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public boolean addItem(String name, String description, Integer quantity, Double price, Integer warehouseId) {
        checkItemDetails(name, description, quantity, price);

        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name).eq("warehouse_id", warehouseId);
        long count = itemMapper.selectCount(queryWrapper);
        if (count > 0) {
            logger.error("(ItemService)同一仓库中物品已存在");
            throw new IllegalArgumentException("同一仓库中物品已存在");
        }

        Date currentTime = new Date();
        Item item = new Item(null, name, description, quantity, price, warehouseId, currentTime, null);
        itemMapper.insert(item);
        logger.info("(ItemService)物品添加成功, id = {}. name = {}, description = {}, quantity = {}, price = {}, warehouseId = {}, createTime = {}",
                item.getId(), item.getName(), item.getDescription(), item.getQuantity(), item.getPrice(), item.getWarehouseId(), item.getCreateTime());

        return true; // Successful addition
    }

    private void checkItemDetails(String name, String description, Integer quantity, Double price) {
        if (name == null || name.trim().isEmpty()) {
            logger.error("(ItemService)物品名称不能为空");
            throw new IllegalArgumentException("物品名称不能为空");
        }

        if (description == null || description.trim().isEmpty()) {
            logger.error("(ItemService)物品描述不能为空");
            throw new IllegalArgumentException("物品描述不能为空");
        }

        if (quantity == null || quantity < 0) {
            logger.error("(ItemService)物品数量不正确");
            throw new IllegalArgumentException("物品数量不正确");
        }

        if (price == null || price < 0) {
            logger.error("(ItemService)物品价格不正确");
            throw new IllegalArgumentException("物品价格不正确");
        }
    }

    @Override
    public boolean updateItem(Integer id, String name, String description, Integer quantity, Double price, Integer warehouseId) {
        checkItemDetails(name, description, quantity, price);

        Item item = itemMapper.selectById(id);
        if (item == null) {
            logger.error("(ItemService)物品不存在");
            throw new IllegalArgumentException("物品不存在");
        }

        item.setName(name);
        item.setDescription(description);
        item.setQuantity(quantity);
        item.setPrice(price);
        item.setWarehouseId(warehouseId);
        item.setUpdateTime(new Date()); // Optionally update creation time on item update
        itemMapper.updateById(item);
        logger.info("(ItemService)物品信息更新成功, id = {}. name = {}, description = {}, quantity = {}, price = {}, warehouseId = {}, updateTime = {}",
                item.getId(), item.getName(), item.getDescription(), item.getQuantity(), item.getPrice(), item.getWarehouseId(), item.getUpdateTime());

        return true; // Successful update
    }

    @Override
    public Item findItemById(Integer id) {
        if (id == null) {
            logger.error("(ItemService)物品ID不能为空");
            throw new IllegalArgumentException("物品ID不能为空");
        }
        return itemMapper.selectById(id);
    }

    @Override
    public List<Item> findAllItems() {
        List<Item> items = itemMapper.selectList(null);
        logger.info("(ItemService)获取物品列表, size = {}, items = {}", items.size(), items);
        return items;
    }

    @Override
    public boolean deleteItem(Integer id) {
        if (id == null) {
            logger.error("(ItemService)物品ID不能为空");
            throw new IllegalArgumentException("物品ID不能为空");
        }
        return itemMapper.deleteById(id) > 0;
    }
}
