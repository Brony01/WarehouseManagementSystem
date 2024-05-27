package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.config.SecurityConstant;
import com.java.warehousemanagementsystem.config.SecurityUserDetails;
import com.java.warehousemanagementsystem.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class SecurityUserDetailsService implements ReactiveUserDetailsService
{

    @Resource
    private UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("用户名或密码错误"))))
                .map(user -> {
                    List<SecurityConstant.PERMISSION> permissions = Collections.singletonList(SecurityConstant.PERMISSION.USER);
                    return SecurityUserDetails.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .permissions(permissions)
                            .build();
                });
    }
}
