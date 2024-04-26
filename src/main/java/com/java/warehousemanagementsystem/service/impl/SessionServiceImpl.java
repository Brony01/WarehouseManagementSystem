package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.mapper.UserMapper;
import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.SessionService;
import com.java.warehousemanagementsystem.utils.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionServiceImpl implements SessionService
{
    @Resource
    UserMapper userMapper;

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> loginSession(String username, String password)
    {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(authenticate == null)
        {
            return null;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        Map<String, String> map = new HashMap<>();

        map.put("token", JwtUtils.generateToken(username, user.getVersion()));
        return map;
    }

    @Override
    public String logoutSession(String username)
    {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        user.setVersion(user.getVersion() + 1);
        userMapper.updateById(user);
        return "登出成功！";
    }
}
