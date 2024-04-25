package com.java.warehousemanagementsystem.service;


import java.util.Map;

public interface UserService
{
    Map<String, String> register(String username, String password, String confirmedPassword);

    Map<String, String> login(String username, String password);

    Map<String, String> logout(String username);

    Map<String, String> update(String username, String password, String confirmedPassword);

    Map<String, String> delete(String username);

    Map<String, String> query(String username);
}
