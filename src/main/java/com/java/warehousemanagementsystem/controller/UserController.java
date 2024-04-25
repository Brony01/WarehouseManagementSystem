package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user") // Uncommented and specified to clearly define the routing path
public class UserController {
    @Resource
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping
    @ResponseBody
    public ResponseResult<?> register(
            @RequestParam @Parameter(description = "用户名") String username,
            @RequestParam @Parameter(description = "密码") String password,
            @RequestParam @Parameter(description = "确认密码") String confirmedPassword) {
        if (userService.register(username, password, confirmedPassword)) {
            return ResponseResult.success("用户注册成功");
        } else {
            return ResponseResult.failure(400, "用户注册失败");
        }
    }

    @Operation(summary = "更新用户数据")
    @PutMapping
    @ResponseBody
    public ResponseResult<?> update(
            @RequestParam @Parameter(description = "用户id") Integer id,
            @RequestParam @Parameter(description = "用户名") String username,
            @RequestParam @Parameter(description = "密码") String password,
            @RequestParam @Parameter(description = "确认密码") String confirmedPassword) {
        if (userService.updateUser(id, username, password, confirmedPassword)) {
            return ResponseResult.success("用户数据更新成功");
        } else {
            return ResponseResult.failure(400, "用户数据更新失败");
        }
    }

    @Operation(summary = "根据id查找用户")
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseResult<User> findUserById(
            @PathVariable @Parameter(description = "用户id") Integer id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseResult.failure(404, "未找到用户");
        }
        return ResponseResult.success(user);
    }

    @Operation(summary = "获取用户列表")
    @GetMapping
    @ResponseBody
    public ResponseResult<List<User>> getList() {
        List<User> users = userService.findAllUser();
        return ResponseResult.success(users);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseResult<?> delete(
            @PathVariable @Parameter(description = "用户id") Integer id) {
        if (!userService.deleteUser(id)) {
            return ResponseResult.failure(404, "未找到用户");
        }
        return ResponseResult.success("用户删除成功");
    }
}
