package com.java.warehousemanagementsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.warehousemanagementsystem.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
