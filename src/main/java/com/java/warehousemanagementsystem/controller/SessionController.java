package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.service.SessionService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Tag(name = "会话管理", description = "会话管理的相关操作")
@RestController
@RequestMapping("/session")
public class SessionController {
    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    @Resource
    private SessionService sessionService;

    @Operation(summary = "用户登录")
    @ApiResponse(responseCode = "200", description = "登录成功")
    @ApiResponse(responseCode = "400", description = "登录失败")
    @PostMapping("")
    public Mono<ResponseResult<Map<String, String>>> login(@RequestParam @Parameter(name = "username", description = "用户名") String username,
                                                           @RequestParam @Parameter(name = "password", description = "密码") String password) {
        logger.info("(SessionController)用户登录, username = {}, password = {}", username, password);
        return sessionService.loginSession(username, password)
                .map(ResponseResult::success)
                .onErrorResume(e -> {
                    logger.error("(SessionController)登录失败: {}", e.getMessage());
                    return Mono.just(ResponseResult.failure(400, "登录失败: " + e.getMessage()));
                });
    }

    @Operation(summary = "用户登出")
    @ApiResponse(responseCode = "200", description = "登出成功")
    @ApiResponse(responseCode = "400", description = "登出失败")
    @DeleteMapping("")
    public Mono<ResponseResult<String>> logout(@RequestParam @Parameter(name = "username", description = "用户名") String username) {
        logger.info("(SessionController)用户登出, username = {}", username);
        return sessionService.logoutSession(username)
                .map(ResponseResult::success)
                .onErrorResume(e -> {
                    logger.error("(SessionController)登出失败: {}", e.getMessage());
                    return Mono.just(ResponseResult.failure(400, "登出失败: " + e.getMessage()));
                });
    }

    @Operation(summary = "测试admin")
    @PostMapping("/test")
    public Mono<String> test() {
        return Mono.just("admin");
    }

    @Operation(summary = "测试user")
    @PostMapping("/test2")
    public Mono<String> test2() {
        return Mono.just("user");
    }
}
