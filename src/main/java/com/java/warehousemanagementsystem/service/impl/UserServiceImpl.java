package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.mapper.UserMapper;
import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Mono<Boolean> register(String username, String password, String confirmedPassword) {
        return Mono.fromCallable(() -> {
            if (!password.equals(confirmedPassword)) {
                throw new IllegalArgumentException("Passwords do not match");
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            return userMapper.insert(user) > 0;
        });
    }

    @Override
    public Mono<Boolean> updateUser(User user) {
        return Mono.fromCallable(() -> userMapper.updateById(user) > 0);
    }

    @Override
    public Mono<User> findUserById(Integer id) {
        return Mono.fromCallable(() -> {
            if (id == null) {
                throw new IllegalArgumentException("User id cannot be null");
            }
            User user = userMapper.selectById(id);
            if (user != null) {
                user.setPassword(null);
            }
            return user;
        });
    }

    @Override
    public Flux<User> findAllUser() {
        return Flux.defer(() -> {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "username", "version");
            return Flux.fromIterable(userMapper.selectList(queryWrapper));
        });
    }

    @Override
    public Mono<Boolean> deleteUser(Integer id) {
        return Mono.fromCallable(() -> {
            if (id == null) {
                throw new IllegalArgumentException("User id cannot be null");
            }
            return userMapper.deleteById(id) > 0;
        });
    }
}
