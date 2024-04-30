package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user") // Uncommented and specified to clearly define the routing path
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping()
    @ResponseBody
    public ResponseResult<?> register(
            @RequestParam @Parameter(description = "用户名") String username,
            @RequestParam @Parameter(description = "密码") String password,
            @RequestParam @Parameter(description = "确认密码") String confirmedPassword) {
        if (userService.register(username, password, confirmedPassword)) {
            logger.info("(UserController)用户注册成功");
            return ResponseResult.success("用户注册成功");
        } else {
            logger.error("(UserController)用户注册失败");
            return ResponseResult.failure(400, "用户注册失败");
        }
    }

    @Operation(summary = "更新用户数据")
    @PutMapping()
    @ResponseBody
    public ResponseResult<?> update(
            @RequestParam @Parameter(description = "用户名") String username,
            @RequestParam @Parameter(description = "密码") String password,
            @RequestParam @Parameter(description = "确认密码") String confirmedPassword) {
        if (userService.updateUser(username, password, confirmedPassword)) {
            logger.info("(UserController)用户数据更新成功");
            return ResponseResult.success("用户数据更新成功");
        } else {
            logger.error("(UserController)用户数据更新失败");
            return ResponseResult.failure(400, "用户数据更新失败");
        }
    }

    @Operation(summary = "根据id查找用户")
    @GetMapping("/{id}")
    @ResponseBody
    @Cacheable(value = "user", key = "#id")
    public ResponseResult<User> findUserById(
            @PathVariable @Parameter(description = "用户id") Integer id) {
        User user = userService.findUserById(id);
        if (user == null) {
            logger.error("(UserController)未找到用户");
            return ResponseResult.failure(404, "未找到用户");
        }

        logger.info("(UserController)用户查找成功");
        return ResponseResult.success(user);
    }

    @Operation(summary = "获取用户列表")
    @GetMapping()
    @ResponseBody
    @Cacheable(value = "userList")
    public ResponseResult<List<User>> getList() {
        List<User> users = userService.findAllUser();
        logger.info("(UserController)获取用户列表, size = {}, users = {}", users.size(), users);
        return ResponseResult.success(users);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @ResponseBody
    @CacheEvict(value = "user", key = "#id")
    public ResponseResult<?> delete(
            @PathVariable @Parameter(description = "用户id") Integer id) {
        if (!userService.deleteUser(id)) {
            logger.error("(UserController)未找到用户");
            return ResponseResult.failure(404, "未找到用户");
        }
        logger.info("(UserController)用户删除成功");
        return ResponseResult.success("用户删除成功");
    }
}
