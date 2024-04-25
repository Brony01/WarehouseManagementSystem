package com.java.warehousemanagementsystem.controller;


import com.java.warehousemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    UserService userService;

    @PostMapping("")
    public Map<String, String> register(@RequestParam  String username,
                                        @RequestParam  String password,
                                        @RequestParam  String confirmedPassword)
    {
        return userService.register(username, password, confirmedPassword);
    }
}
