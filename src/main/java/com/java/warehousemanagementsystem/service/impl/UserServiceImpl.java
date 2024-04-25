package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.mapper.UserMapper;
import com.java.warehousemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean register(String username, String password, String confirmedPassword)
            throws IllegalArgumentException {
        check(username, password, confirmedPassword);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new IllegalArgumentException("用户已存在");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, encodedPassword);
        userMapper.insert(user);

        return true; // Successful registration
    }

    private void check(String username, String password, String confirmedPassword)
    {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        if (username.length() > 100) {
            throw new IllegalArgumentException("用户名过长");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        if (password.length() > 100) {
            throw new IllegalArgumentException("密码过长");
        }

        if (!password.equals(confirmedPassword)) {
            throw new IllegalArgumentException("密码不一致");
        }
    }

    @Override
    public boolean updateUser(int id, String username, String password, String confirmedPassword)
            throws IllegalArgumentException {
        check(username, password, confirmedPassword);

        User user = userMapper.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        String encodedPassword = passwordEncoder.encode(password);
        user.setUsername(username);
        user.setPassword(encodedPassword);
        userMapper.updateById(user);

        return true; // Successful update
    }

    @Override
    public User findUserById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户id不能为空");
        }
        return userMapper.selectById(id);
    }

    @Override
    public List<User> findAllUser() {
        return userMapper.selectList(null);
    }

    @Override
    public boolean deleteUser(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户id不能为空");
        }
        return userMapper.deleteById(id) > 0;
    }
}
