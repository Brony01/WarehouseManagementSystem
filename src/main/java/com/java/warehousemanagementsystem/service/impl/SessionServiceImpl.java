package com.java.warehousemanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.mapper.UserMapper;
import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.SessionService;
import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.utils.JwtUtils;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    // Adding setter for testing
    @Setter
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
            logger.error("(SessionServiceImpl)登录失败,authenticate == null");
            return null;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        Map<String, String> map = new HashMap<>();

        map.put("token", JwtUtils.generateToken(username, user.getVersion()));
        logger.info("(SessionServiceImpl)登录成功, token = {}", map.get("token"));
        return map;
    }

    @Override
    public String logoutSession(String username)
    {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        user.setVersion(user.getVersion() + 1);
        userMapper.updateById(user);
        logger.info("(SessionServiceImpl)登出成功");
        return "登出成功！";
    }
}
