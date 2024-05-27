package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "用户管理", description = "用户管理的相关操作")
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户注册")
    @PostMapping()
    @ApiResponse(responseCode = "200", description = "注册成功")
    @ApiResponse(responseCode = "400", description = "注册失败")
    @ResponseBody
    public Mono<ResponseResult<String>> register(
            @RequestParam @Parameter(description = "用户名") String username,
            @RequestParam @Parameter(description = "密码") String password,
            @RequestParam @Parameter(description = "确认密码") String confirmedPassword) {
        return userService.register(username, password, confirmedPassword)
                .map(success -> success ? ResponseResult.success("用户注册成功") : ResponseResult.failure(400, "用户注册失败"));
    }

    @Operation(summary = "更新用户数据")
    @PutMapping()
    @ApiResponse(responseCode = "200", description = "用户数据更新成功")
    @ApiResponse(responseCode = "400", description = "用户数据更新失败")
    @ResponseBody
    public Mono<ResponseResult<String>> updateUser(@RequestBody User user) {
        return userService.updateUser(user)
                .map(success -> success ? ResponseResult.success("用户数据更新成功") : ResponseResult.failure(400, "用户数据更新失败"));
    }

    @Operation(summary = "根据ID查找用户")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "用户查找成功")
    @ApiResponse(responseCode = "400", description = "用户查找失败")
    @ResponseBody
    public Mono<ResponseResult<User>> getUserById(@PathVariable Integer id) {
        return userService.findUserById(id)
                .map(ResponseResult::success);
    }

    @Operation(summary = "获取所有用户")
    @GetMapping()
    @ApiResponse(responseCode = "200", description = "用户列表获取成功")
    @ApiResponse(responseCode = "400", description = "用户列表获取失败")
    @ResponseBody
    public Flux<User> getAllUsers() {
        return userService.findAllUser();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "用户删除成功")
    @ApiResponse(responseCode = "400", description = "用户删除失败")
    @ResponseBody
    public Mono<ResponseResult<String>> deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id)
                .map(success -> success ? ResponseResult.success("用户删除成功") : ResponseResult.failure(400, "用户删除失败"));
    }
}
