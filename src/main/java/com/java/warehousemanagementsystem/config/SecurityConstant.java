package com.java.warehousemanagementsystem.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.warehousemanagementsystem.mapper.UserMapper;
import com.java.warehousemanagementsystem.pojo.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SecurityConstant
{

    @Autowired
    UserMapper userMapper;


    /**
     * 模拟用户数据。key：用户名，value：密码
     */
    public static final Map<String, String> USER_MAP = new ConcurrentHashMap<>();

    /**
     * 模拟权限数据。key：接口地址，value：所需权限
     */
    public static final Map<String, ConfigAttribute> PERMISSION_MAP = new ConcurrentHashMap<>();

    /**
     * 用户权限数据。key：用户名，value：权限
     */
    public static final Map<String, List<PERMISSION>> USER_PERMISSION_MAP = new ConcurrentHashMap<>();

    /**
     * 白名单
     */
    public static final String[] WHITELIST = {"/session", "/user"};

    @PostConstruct
    public void init()
    {
        /*
          账号：
          admin admin
          user user
          user1 user1
          user2 user2
         */

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user : users)
        {
            USER_MAP.put(user.getUsername(), user.getPassword());

            if(user.getUsername().equals("admin"))
            {
                //赋予当前用户管理员权限
                USER_PERMISSION_MAP.put(user.getUsername(), List.of(PERMISSION.ADMIN, PERMISSION.USER));
            }
            else
            {
                //普通用户
                USER_PERMISSION_MAP.put(user.getUsername(), List.of(PERMISSION.USER));
            }
        }
    }

    public void update()
    {
        USER_MAP.clear();
        USER_PERMISSION_MAP.clear();
        init();
    }

    static
    {
        // 填充接口权限 需要权限的接口都要写下面

        // 只允许admin访问
        PERMISSION_MAP.put("/session/test", new SecurityConfig(PERMISSION.ADMIN.getValue()));

        // 允许user访问
        PERMISSION_MAP.put("/session/test2", new SecurityConfig(PERMISSION.USER.getValue()));
    }

    /**
     * 模拟权限
     */
    @Getter
    public enum PERMISSION
    {
        ADMIN("admin"), USER("user");

        private final String value;

        PERMISSION(String value)
        {
            this.value = value;
        }
    }
}
