package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.Item;

import java.util.List;
//public class Item implements Serializable{
//    @TableId(type = IdType.AUTO)
//    private Integer id;
//    private String name;
//    private String description;
//    private Integer quantity;
//    private Double price;
//    private Integer warehouseId;
//    private Date createTime;
//}
public interface ItemService {
    List<Item> findAllItems();
    boolean addItem(String name, String description, Integer quantity, Double price, Integer warehouseId);
    Item findItemById(Integer id);
    boolean updateItem(Integer id, String name, String description, Integer quantity, Double price, Integer warehouseId);
    boolean deleteItem(Integer id);
}
