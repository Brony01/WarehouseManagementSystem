package com.java.warehousemanagementsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.warehousemanagementsystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>
{
    public List<User> findAllUser();

    public void insertUser(String username, String password);

    public void deleteUser(int id);

    public void updateUser(Integer id, String username, String password, String confirmedPassword);

    public User findUserById(Integer id);
}
