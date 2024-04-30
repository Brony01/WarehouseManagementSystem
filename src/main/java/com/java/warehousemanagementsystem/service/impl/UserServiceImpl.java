package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.mapper.UserMapper;
import com.java.warehousemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
            logger.error("(UserService)用户已存在");
            throw new IllegalArgumentException("用户已存在");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, encodedPassword, 1);
        userMapper.insert(user);
        logger.info("(UserService)用户注册成功, id = {}. username = {}", user.getId(), user.getUsername());

        return true; // Successful registration
    }

    private void check(String username, String password, String confirmedPassword) {
        if (username == null || username.trim().isEmpty()) {
            logger.error("(UserService)用户名不能为空");
            throw new IllegalArgumentException("用户名不能为空");
        }

        if (username.length() > 100) {
            logger.error("(UserService)用户名过长");
            throw new IllegalArgumentException("用户名过长");
        }

        if (password == null || password.isEmpty()) {
            logger.error("(UserService)密码不能为空");
            throw new IllegalArgumentException("密码不能为空");
        }

        if (password.length() > 100) {
            logger.error("(UserService)密码过长");
            throw new IllegalArgumentException("密码过长");
        }

        if (!password.equals(confirmedPassword)) {
            logger.error("(UserService)密码不一致");
            throw new IllegalArgumentException("密码不一致");
        }
    }

    @Override
    public boolean updateUser(String username, String password, String confirmedPassword)
            throws IllegalArgumentException {
        check(username, password, confirmedPassword);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            logger.error("(UserService)用户不存在");
            throw new IllegalArgumentException("用户不存在");
        }

        String encodedPassword = passwordEncoder.encode(password);
        user.setUsername(username);
        user.setPassword(encodedPassword);
        userMapper.updateById(user);
        logger.info("(UserService)用户数据更新成功, id = {}. username = {}", user.getId(), user.getUsername());

        return true; // Successful update
    }

    @Override
    public User findUserById(Integer id) {
        if (id == null) {
            logger.error("(UserService)用户id不能为空");
            throw new IllegalArgumentException("用户id不能为空");
        }
        User user = userMapper.selectById(id);
        user.setPassword(null);
        return user;
    }

    @Override
    public List<User> findAllUser() {
        logger.info("(UserService)获取用户列表, size = {}, users = {}", userMapper.selectList(null).size(), userMapper.selectList(null));
        QueryWrapper<User> queryWrapper  = new QueryWrapper<>();
        queryWrapper.select("id", "username", "version");
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public boolean deleteUser(Integer id) {
        if (id == null) {
            logger.error("(UserService)用户id不能为空");
            throw new IllegalArgumentException("用户id不能为空");
        }
        return userMapper.deleteById(id) > 0;
    }
}
