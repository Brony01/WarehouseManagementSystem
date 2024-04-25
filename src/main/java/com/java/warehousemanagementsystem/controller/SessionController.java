package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.service.SessionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/test")
    public String test()
    {
        return "admin";
    }

    @PostMapping("/test2")
    public String test2()
    {
        return "user";
    }
}
