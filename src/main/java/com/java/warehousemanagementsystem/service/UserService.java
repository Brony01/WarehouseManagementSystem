package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.User;

import java.util.List;

public interface UserService {
    boolean register(String username, String password, String confirmedPassword);

    boolean updateUser(String username, String password, String confirmedPassword);

    User findUserById(Integer id);

    List<User> findAllUser();

    boolean deleteUser(Integer id);
}
