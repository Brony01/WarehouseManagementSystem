package com.java.warehousemanagementsystem.controller;


import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(summary = "用户注册")
    @RequestMapping(value = "/register" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult register(@RequestParam @Parameter(name = "username", description = "用户名") String username,
                                   @RequestParam @Parameter(name = "password", description = "密码") String password,
                                   @RequestParam @Parameter(name = "confirmedPassword", description = "确认密码") String confirmedPassword) {
        userService.register(username, password, confirmedPassword);
        return ResponseResult.okResult();
    }
}
