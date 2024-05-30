package com.java.warehousemanagementsystem.service.impl;

import com.java.warehousemanagementsystem.mapper.UserMapper;
import com.java.warehousemanagementsystem.pojo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {
        User user = new User();
        when(userMapper.insert(any(User.class))).thenReturn(1);

        Mono<Boolean> result = userService.register("test", "password", "password");
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        Mono<Boolean> result = userService.updateUser(user);
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testFindUserById() {
        User user = new User();
        user.setId(1);
        when(userMapper.selectById(1)).thenReturn(user);

        Mono<User> result = userService.findUserById(1);
        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void testFindAllUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        users.add(user);

        when(userMapper.selectList(any())).thenReturn(users);

        Flux<User> result = userService.findAllUser();
        StepVerifier.create(result)
                .expectNextSequence(users)
                .verifyComplete();
    }

    @Test
    void testDeleteUser() {
        when(userMapper.deleteById(1)).thenReturn(1);

        Mono<Boolean> result = userService.deleteUser(1);
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }
}
