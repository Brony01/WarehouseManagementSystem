package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.annotation.RateLimit;
import com.java.warehousemanagementsystem.service.SessionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/session")
public class SessionController
{
    @Resource
    SessionService sessionService;

    @PostMapping("")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password)
    {
        return sessionService.loginSession(username, password);
    }

    @RateLimit
    @PostMapping("/test")
    public String test()
    {
        return "admin";
    }

    @RateLimit
    @PostMapping("/test2")
    public String test2()
    {
        return "user";
    }

    @GetMapping("/test3")
    public String test3()
    {
        return "user";
    }
}
