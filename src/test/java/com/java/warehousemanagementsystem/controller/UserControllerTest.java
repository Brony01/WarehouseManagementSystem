package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.verify;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testRegisterSuccess() throws Exception {
        // 模拟服务层的行为
        given(userService.register("user1", "password", "password")).willReturn(true);

        // 执行请求并验证结果
        mockMvc.perform(post("/user/register")
                        .param("username", "user1")
                        .param("password", "password")
                        .param("confirmedPassword", "password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("用户注册成功"));
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
        given(userService.updateUser(1, "user1", "password", "password")).willReturn(true);

        mockMvc.perform(put("/user/update")
                        .param("id", "1")
                        .param("username", "user1")
                        .param("password", "password")
                        .param("confirmedPassword", "password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("用户数据更新成功"));
    }

    @Test
    public void testFindUserByIdNotFound() throws Exception {
        given(userService.findUserById(1)).willReturn(null);

        mockMvc.perform(get("/user/findUserById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("未找到用户"));
    }

    @Test
    public void testGetList() throws Exception {
        List<User> users = Arrays.asList(new User(1, "user1", "encodedPassword"), new User(2, "user2", "encodedPassword"));
        given(userService.findAllUser()).willReturn(users);

        mockMvc.perform(get("/user/getList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].username").value("user1"))
                .andExpect(jsonPath("$.data[1].username").value("user2"));
    }

    @Test
    public void testDeleteUserFailure() throws Exception {
        given(userService.deleteUser(1)).willReturn(false);

        mockMvc.perform(delete("/user/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("未找到用户"));
    }
}
