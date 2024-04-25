package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testRegisterSuccess() throws Exception {
        given(userService.register("testUser", "password", "password")).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .param("username", "testUser")
                        .param("password", "password")
                        .param("confirmedPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("用户注册成功"));
    }

    @Test
    void testRegisterFailure() throws Exception {
        given(userService.register("testUser", "password", "password")).willReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .param("username", "testUser")
                        .param("password", "password")
                        .param("confirmedPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("用户注册失败"));
    }

    // Similar structure for update, delete, findUserById, and getList
    @Test
    void testUpdateSuccess() throws Exception {
        given(userService.updateUser(1, "testUser", "password", "password")).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .param("id", "1")
                        .param("username", "testUser")
                        .param("password", "password")
                        .param("confirmedPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("用户数据更新成功"));
    }

    @Test
    void testUpdateFailure() throws Exception {
        given(userService.updateUser(1, "testUser", "password", "password")).willReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .param("id", "1")
                        .param("username", "testUser")
                        .param("password", "password")
                        .param("confirmedPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("用户数据更新失败"));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        given(userService.findUserById(1)).willReturn(new User(1, "testUser", "password"));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("testUser"))
                .andExpect(jsonPath("$.data.password").value("password"));
    }

    @Test
    void testFindUserByIdFailure() throws Exception {
        given(userService.findUserById(1)).willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("未找到用户"));
    }

    @Test
    void testGetList() throws Exception {
        List<User> users = Arrays.asList(new User(1, "testUser1", "password1"),
                                         new User(2, "testUser2", "password2"));
        given(userService.findAllUser()).willReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("testUser1"))
                .andExpect(jsonPath("$.data[0].password").value("password1"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].username").value("testUser2"))
                .andExpect(jsonPath("$.data[1].password").value("password2"));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        given(userService.deleteUser(1)).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("用户删除成功"));
    }

    @Test
    void testDeleteUserFailure() throws Exception {
        given(userService.deleteUser(1)).willReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("未找到用户"));
    }
}
