package com.java.warehousemanagementsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.warehousemanagementsystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
