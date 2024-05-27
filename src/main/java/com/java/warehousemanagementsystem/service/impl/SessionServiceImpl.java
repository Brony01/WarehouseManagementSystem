package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.repository.UserRepository;
import com.java.warehousemanagementsystem.service.SessionService;
import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.utils.JwtUtils;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionServiceImpl implements SessionService {
    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Resource
    private UserRepository userRepository;

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Map<String, String>> loginSession(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return Mono.just(authenticationToken)
                .flatMap(token -> {
                    Authentication authenticate = authenticationManager.authenticate(token);
                    if (authenticate == null) {
                        logger.error("(SessionServiceImpl)登录失败,authenticate == null");
                        return Mono.error(new IllegalArgumentException("登录失败"));
                    }
                    return userRepository.findByUsername(username)
                            .flatMap(user -> {
                                Map<String, String> map = new HashMap<>();
                                map.put("token", JwtUtils.generateToken(username, user.getVersion()));
                                logger.info("(SessionServiceImpl)登录成功, token = {}", map.get("token"));
                                return Mono.just(map);
                            });
                });
    }

    @Override
    public Mono<String> logoutSession(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    user.setVersion(user.getVersion() + 1);
                    return userRepository.save(user)
                            .doOnSuccess(savedUser -> logger.info("(SessionServiceImpl)登出成功"))
                            .then(Mono.just("登出成功！"));
                });
    }
}
