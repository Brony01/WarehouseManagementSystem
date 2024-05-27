package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "用户管理", description = "用户管理的相关操作")
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping()
    @ApiResponse(responseCode = "200", description = "注册成功")
    @ApiResponse(responseCode = "400", description = "注册失败")
    public Mono<ResponseResult<?>> register(
            @RequestParam @Parameter(description = "用户名") String username,
            @RequestParam @Parameter(description = "密码") String password,
            @RequestParam @Parameter(description = "确认密码") String confirmedPassword) {
        return userService.register(username, password, confirmedPassword)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(UserController)用户注册成功");
                        return Mono.just(ResponseResult.success("用户注册成功"));
                    } else {
                        logger.error("(UserController)用户注册失败");
                        return Mono.just(ResponseResult.failure(400, "用户注册失败"));
                    }
                });
    }

    @Operation(summary = "更新用户数据")
    @PutMapping()
    @ApiResponse(responseCode = "200", description = "用户数据更新成功")
    @ApiResponse(responseCode = "400", description = "用户数据更新失败")
    public Mono<ResponseResult<?>> update(
            @RequestParam @Parameter(description = "用户名") String username,
            @RequestParam @Parameter(description = "密码") String password,
            @RequestParam @Parameter(description = "确认密码") String confirmedPassword) {
        return userService.updateUser(username, password, confirmedPassword)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(UserController)用户数据更新成功");
                        return Mono.just(ResponseResult.success("用户数据更新成功"));
                    } else {
                        logger.error("(UserController)用户数据更新失败");
                        return Mono.just(ResponseResult.failure(400, "用户数据更新失败"));
                    }
                });
    }

    @Operation(summary = "根据id查找用户")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "成功找到用户")
    @ApiResponse(responseCode = "404", description = "未找到用户")
    @ApiResponse(responseCode = "400", description = "查找用户失败")
    @Cacheable(value = "user", key = "#id")
    public Mono<ResponseResult<User>> findUserById(
            @PathVariable @Parameter(description = "用户id") Integer id) {
        return userService.findUserById(id)
                .flatMap(user -> {
                    logger.info("(UserController)用户查找成功");
                    return Mono.just(ResponseResult.success(user));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.error("(UserController)未找到用户");
                    return Mono.just(ResponseResult.failure(404, "未找到用户"));
                }));
    }

    @Operation(summary = "获取用户列表")
    @GetMapping()
    @ApiResponse(responseCode = "200", description = "成功获取所有用户数据")
    @ApiResponse(responseCode = "404", description = "未找到用户")
    @ApiResponse(responseCode = "400", description = "获取用户失败")
    @Cacheable(value = "userList")
    public Mono<ResponseResult<List<User>>> getList() {
        return userService.findAllUsers()
                .collectList()
                .flatMap(users -> {
                    logger.info("(UserController)获取用户列表, size = {}, users = {}", users.size(), users);
                    return Mono.just(ResponseResult.success(users));
                });
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "用户删除成功")
    @ApiResponse(responseCode = "404", description = "未找到用户")
    @ApiResponse(responseCode = "400", description = "用户删除失败")
    @CacheEvict(value = "user", key = "#id")
    public Mono<ResponseResult<?>> delete(
            @PathVariable @Parameter(description = "用户id") Integer id) {
        return userService.deleteUser(id)
                .flatMap(success -> {
                    if (success) {
                        logger.info("(UserController)用户删除成功");
                        return Mono.just(ResponseResult.success("用户删除成功"));
                    } else {
                        logger.error("(UserController)未找到用户");
                        return Mono.just(ResponseResult.failure(404, "未找到用户"));
                    }
                });
    }
}
