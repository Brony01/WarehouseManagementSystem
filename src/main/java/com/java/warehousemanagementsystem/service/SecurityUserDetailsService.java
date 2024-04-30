package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.config.SecurityConstant;
import com.java.warehousemanagementsystem.config.SecurityUserDetails;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取用户信息
        String password = SecurityConstant.USER_MAP.get(username);
        if (StringUtils.isBlank(password)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        // 获取用户权限
        List<SecurityConstant.PERMISSION> permission = SecurityConstant.USER_PERMISSION_MAP.get(username);
        // 返回SecurityUserDetails
        return SecurityUserDetails.builder()
                .username(username)
                .password(password)
                .permissions(permission)
                .build();
    }
}
