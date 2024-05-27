package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.repository.UserRepository;
import com.java.warehousemanagementsystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<Boolean> register(String username, String password, String confirmedPassword) {
        try {
            check(username, password, confirmedPassword);
        } catch (IllegalArgumentException e) {
            return Mono.error(e);
        }

        return userRepository.findByUsername(username)
                .flatMap(existingUser -> {
                    logger.error("(UserService)用户已存在");
                    return Mono.error(new IllegalArgumentException("用户已存在"));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    String encodedPassword = passwordEncoder.encode(password);
                    User user = new User(null, username, encodedPassword, 1);
                    return userRepository.save(user)
                            .doOnSuccess(savedUser -> logger.info("(UserService)用户注册成功, id = {}. username = {}", savedUser.getId(), savedUser.getUsername()))
                            .thenReturn(true);
                })).hasElement();
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
    public Mono<Boolean> updateUser(String username, String password, String confirmedPassword) {
        try {
            check(username, password, confirmedPassword);
        } catch (IllegalArgumentException e) {
            return Mono.error(e);
        }

        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    String encodedPassword = passwordEncoder.encode(password);
                    user.setPassword(encodedPassword);
                    return userRepository.save(user)
                            .doOnSuccess(savedUser -> logger.info("(UserService)用户数据更新成功, id = {}. username = {}", savedUser.getId(), savedUser.getUsername()))
                            .thenReturn(true);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.error("(UserService)用户不存在");
                    return Mono.error(new IllegalArgumentException("用户不存在"));
                }));
    }

    @Override
    public Mono<User> findUserById(Integer id) {
        if (id == null) {
            logger.error("(UserService)用户id不能为空");
            return Mono.error(new IllegalArgumentException("用户id不能为空"));
        }
        return userRepository.findById(id)
                .map(user -> {
                    user.setPassword(null);
                    return user;
                });
    }

    @Override
    public Flux<User> findAllUsers() {
        return userRepository.findAll()
                .doOnNext(user -> user.setPassword(null))
                .doOnComplete(() -> logger.info("(UserService)获取用户列表"));
    }

    @Override
    public Mono<Boolean> deleteUser(Integer id) {
        if (id == null) {
            logger.error("(UserService)用户id不能为空");
            return Mono.error(new IllegalArgumentException("用户id不能为空"));
        }
        return userRepository.deleteById(id)
                .thenReturn(true)
                .doOnSuccess(success -> logger.info("(UserService)用户删除成功, id = {}", id))
                .onErrorReturn(false);
    }
}
