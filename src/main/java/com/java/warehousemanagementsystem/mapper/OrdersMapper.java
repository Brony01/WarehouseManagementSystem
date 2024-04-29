package com.java.warehousemanagementsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders>
{
    @Select("SELECT i.* FROM order_item oi INNER JOIN item i ON oi.item_id = i.id WHERE oi.order_id = #{orderId}")
    List<Item> getItemsByOrderId(Integer Id);
}
