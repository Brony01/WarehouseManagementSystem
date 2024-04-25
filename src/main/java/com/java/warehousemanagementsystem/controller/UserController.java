package com.java.warehousemanagementsystem.controller;


import com.java.warehousemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import com.java.warehousemanagementsystem.vo.ResponseResult;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(summary = "用户注册")
    @RequestMapping(value = "/register" , method = RequestMethod.POST)
    public ResponseResult register(@Parameter(name = "username", description = "用户名") String username,
                                        @Parameter(name = "password", description = "密码") String password,
                                        @Parameter(name = "confirmedPassword", description = "确认密码") String confirmedPassword) {
        userService.register(username, password, confirmedPassword);
        return ResponseResult.okResult();
    }
}
