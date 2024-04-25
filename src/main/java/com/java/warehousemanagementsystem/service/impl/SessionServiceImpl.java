package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.service.SessionService;
import com.java.warehousemanagementsystem.utils.JwtUtils;
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
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> loginSession(String username, String password)
    {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        Map<String, String> map = new HashMap<>();
        map.put("token", JwtUtils.generateToken(username));
        return map;
    }
}
