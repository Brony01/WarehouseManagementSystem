package com.java.warehousemanagementsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.warehousemanagementsystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
