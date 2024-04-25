package com.java.warehousemanagementsystem.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.config.SecurityConstant;
import com.java.warehousemanagementsystem.mapper.UserMapper;
import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecurityConstant securityConstant;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword)
    {
        Map<String, String> map = new HashMap<>();

        if(username == null)
        {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        username = username.trim();
        username = username.trim();
        if (username.isEmpty())
        {
            map.put("error_message", "用户名不能为空");
            return map;
        }


        if(username.length() > 100)
        {
            map.put("error_message", "用户名过长");
            return map;
        }

        if (password == null || confirmedPassword == null) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        if (password.isEmpty() || confirmedPassword.isEmpty()) {
            map.put("error_message", "密码不能为空");
        }

        if(password.length() > 100)
        {
            map.put("error_message", "密码过长");
            return map;
        }

        if(!password.equals(confirmedPassword))
        {
            map.put("error_message", "密码不一致");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty())
        {
            map.put("error_message", "用户已存在");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, encodedPassword);
        userMapper.insert(user);

        map.put("error_message", "success");
        securityConstant.update();
        return map;
    }
}
