package com.java.warehousemanagementsystem.service;


import java.util.Map;

public interface UserService
{
    Map<String, String> register(String username, String password, String confirmedPassword);
}
