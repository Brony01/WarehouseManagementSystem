package com.java.warehousemanagementsystem.service;

import com.java.warehousemanagementsystem.pojo.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Boolean> register(String username, String password, String confirmedPassword);
    Mono<Boolean> updateUser(User user);
    Mono<User> findUserById(Integer id);
    Flux<User> findAllUser();
    Mono<Boolean> deleteUser(Integer id);
}
