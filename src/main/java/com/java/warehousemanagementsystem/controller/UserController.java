package com.java.warehousemanagementsystem.controller;


import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(summary = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult register(@RequestParam @Parameter(name = "username", description = "用户名") String username,
                                   @RequestParam @Parameter(name = "password", description = "密码") String password,
                                   @RequestParam @Parameter(name = "confirmedPassword", description = "确认密码") String confirmedPassword) {
        userService.register(username, password, confirmedPassword);
        return ResponseResult.okResult();
    }

    @Operation(summary = "更新用户数据")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseResult update(@RequestParam @Parameter(name = "id", description = "用户id") Integer id,
                                 @RequestParam @Parameter(name = "username", description = "用户名") String username,
                                 @RequestParam @Parameter(name = "password", description = "密码") String password,
                                 @RequestParam @Parameter(name = "confirmedPassword", description = "确认密码") String confirmedPassword) {
        userService.updateUser(id, username, password, confirmedPassword);
        return ResponseResult.okResult();
    }

    @Operation(summary = "根据id查找用户")
    @RequestMapping(value = "/findUserById", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult findUserById(@RequestParam @Parameter(name = "id", description = "用户id") Integer id) {
        return userService.findUserById(id);
    }

    @Operation(summary = "获取用户列表")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<List<User>> getList() {
        return userService.findAllUser();
    }

    @Operation(summary = "删除用户")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseResult delete(@RequestParam @Parameter(name = "id", description = "用户id") Integer id) {
        userService.deleteUser(id);
        return ResponseResult.okResult();
    }
}
