package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.service.SessionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/session")
public class SessionController
{
    @Resource
    SessionService sessionService;

    @Operation(summary = "用户登录")
    @PostMapping("")
    public Map<String, String> login(@Parameter(name = "username", description = "用户名") String username,
                                     @Parameter(name = "password", description = "密码") String password)
    {
        return sessionService.loginSession(username, password);
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
        return "user";
    }
}
