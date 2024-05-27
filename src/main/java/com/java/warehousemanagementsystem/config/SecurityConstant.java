package com.java.warehousemanagementsystem.config;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SecurityConstant {

    @Autowired
    private UserRepository userRepository;

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
    public void init() {
        initUsers()
                .doOnSuccess(success -> System.out.println("User data initialized successfully"))
                .doOnError(error -> System.err.println("User data initialization failed: " + error.getMessage()))
                .subscribe();
    }

    public Mono<Void> initUsers() {
        return userRepository.findAll()
                .collectList()
                .flatMap(users -> {
                    users.forEach(user -> {
                        USER_MAP.put(user.getUsername(), user.getPassword());
                        if ("admin".equals(user.getUsername())) {
                            //赋予当前用户管理员权限
                            USER_PERMISSION_MAP.put(user.getUsername(), List.of(PERMISSION.ADMIN, PERMISSION.USER));
                        } else {
                            //普通用户
                            USER_PERMISSION_MAP.put(user.getUsername(), List.of(PERMISSION.USER));
                        }
                    });
                    return Mono.empty();
                });
    }

    public Mono<Void> update() {
        USER_MAP.clear();
        USER_PERMISSION_MAP.clear();
        return initUsers();
    }

    static {
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
    public enum PERMISSION {
        ADMIN("admin"), USER("user");

        private final String value;

        PERMISSION(String value) {
            this.value = value;
        }
    }
}
