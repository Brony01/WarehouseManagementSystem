package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.vo.ResponseResult;

import java.util.List;

public interface UserService
{
    ResponseResult register(String username, String password, String confirmedPassword);

    ResponseResult updateUser(int id, String username, String password, String confirmedPassword);

    ResponseResult<User> findUserById(Integer id);

    ResponseResult<List<User>> findAllUser();

    ResponseResult deleteUser(Integer id);
}
