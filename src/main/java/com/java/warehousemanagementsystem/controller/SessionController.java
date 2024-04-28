package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.service.SessionService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/session")
public class SessionController
{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    SessionService sessionService;

    @Operation(summary = "用户登录")
    @PostMapping("")
    public ResponseResult<Object> login(@Parameter(name = "username", description = "用户名") String username,
                                @Parameter(name = "password", description = "密码") String password)
    {
        logger.info("(SessionController)用户登录, username = {}, password = {}", username, password);
        return ResponseResult.success(sessionService.loginSession(username, password));
    }

    @Operation(summary = "用户登出")
    @DeleteMapping("")
    public ResponseResult<Object> logout(@Parameter(name = "username", description = "用户名") String username)
    {
        logger.info("(SessionController)用户登出, username = {}", username);
        return ResponseResult.success(sessionService.logoutSession(username));
    }

    @Operation(summary = "测试admin")
    @PostMapping("/test")
    public String test()
    {
        return "admin";
    }

    @Operation(summary = "测试user")
    @PostMapping("/test2")
    public String test2()
    {
        System.out.println(1);
        return "user";
    }
}
