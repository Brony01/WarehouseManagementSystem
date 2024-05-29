package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@WebFluxTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    void testGetAllUsers() {
        User user1 = new User(1, "User1", "Password1", 1);
        User user2 = new User(2, "User2", "Password2", 1);
        List<User> users = Arrays.asList(user1, user2);

        given(userService.findAllUser()).willReturn(Flux.fromIterable(users));

        webTestClient.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class).isEqualTo(users);
    }

    @Test
    void testRegisterUser() {
        given(userService.register("NewUser", "Password1", "Password1"))
                .willReturn(Mono.just(true));

        webTestClient.post().uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(Map.of("username", "NewUser", "password", "Password1", "confirmedPassword", "Password1")))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getMsg().equals("注册成功");
                });
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User(1, "UpdatedUser", "UpdatedPassword", 1);
        given(userService.updateUser(updatedUser)).willReturn(Mono.just(true));

        webTestClient.put().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(updatedUser))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getMsg().equals("更新用户成功");
                });
    }

    @Test
    void testGetUserById() {
        User user = new User(1, "User1", "Password1", 1);

        given(userService.findUserById(1)).willReturn(Mono.just(user));

        webTestClient.get().uri("/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getData().equals(user);
                });
    }

    @Test
    void testDeleteUser() {
        given(userService.deleteUser(1)).willReturn(Mono.just(true));

        webTestClient.delete().uri("/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getMsg().equals("成功删除用户");
                });
    }
}
