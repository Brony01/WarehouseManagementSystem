package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.config.SecurityConfig;
import com.java.warehousemanagementsystem.service.SessionService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@WebFluxTest(SessionController.class)
@Import(SecurityConfig.class)
public class SessionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SessionService sessionService;

    @BeforeEach
    public void setUp() {
        this.webTestClient = this.webTestClient.mutateWith(SecurityMockServerConfigurers.mockUser());
    }

    @Test
    void testLoginSession() {
        given(sessionService.loginSession("user1", "password1"))
                .willReturn(Mono.just(Map.of("token", "123456789")));

        webTestClient.post().uri("/sessions/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(Map.of("username", "user1", "password", "password1")))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    //assert response.getMsg().equals("Login successful");
                });
    }

    @Test
    void testLogoutSession() {
        given(sessionService.logoutSession("user1"))
                .willReturn(Mono.just("Logout successful"));

        webTestClient.post().uri("/sessions/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(Map.of("username", "user1")))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    //assert response.getMsg().equals("Logout successful");
                });
    }
}
