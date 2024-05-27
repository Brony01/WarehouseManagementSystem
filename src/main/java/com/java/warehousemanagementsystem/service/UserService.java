package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {
    Mono<Boolean> register(String username, String password, String confirmedPassword);

    Mono<Boolean> updateUser(String username, String password, String confirmedPassword);

    Mono<User> findUserById(Integer id);

    Flux<User> findAllUsers();

    Mono<Boolean> deleteUser(Integer id);
}
