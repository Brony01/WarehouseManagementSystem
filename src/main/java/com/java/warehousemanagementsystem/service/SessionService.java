package com.java.warehousemanagementsystem.service;

import java.util.Map;


import reactor.core.publisher.Mono;
import java.util.Map;

public interface SessionService {
    Mono<Map<String, String>> loginSession(String username, String password);

    Mono<String> logoutSession(String username);
}
