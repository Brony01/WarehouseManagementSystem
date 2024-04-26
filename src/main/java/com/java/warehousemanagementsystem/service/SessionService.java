package com.java.warehousemanagementsystem.service;

import java.util.Map;


public interface SessionService
{
    Map<String, String> loginSession(String username, String password);

    String logoutSession(String username);
}
