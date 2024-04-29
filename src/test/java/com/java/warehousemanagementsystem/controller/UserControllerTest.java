package com.java.warehousemanagementsystem.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.service.UserService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import com.java.warehousemanagementsystem.enums.AppHttpCodeEnum;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testRegister() {
        String username = "user";
        String password = "pass";
        String confirmedPassword = "pass";
        when(userService.register(username, password, confirmedPassword)).thenReturn(true);

        ResponseResult<?> result = userController.register(username, password, confirmedPassword);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    void testRegisterFailure() {
        String username = "user";
        String password = "pass";
        String confirmedPassword = "fail";
        when(userService.register(username, password, confirmedPassword)).thenReturn(false);

        ResponseResult<?> result = userController.register(username, password, confirmedPassword);

        assertEquals(400, result.getCode());
        assertEquals("用户注册失败", result.getMsg());
    }

    @Test
    void testUpdate() {
        Integer id = 1;
        String username = "updatedUser";
        String password = "updatedPass";
        String confirmedPassword = "updatedPass";
        when(userService.updateUser(id, username, password, confirmedPassword)).thenReturn(true);

        ResponseResult<?> result = userController.update(id, username, password, confirmedPassword);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    void testFindUserById() {
        Integer id = 1;
        User user = new User();
        user.setId(id);
        user.setUsername("user");
        when(userService.findUserById(id)).thenReturn(user);

        ResponseResult<User> result = userController.findUserById(id);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertNotNull(result.getData());
        assertEquals("user", result.getData().getUsername());
    }

    @Test
    void testFindUserByIdNotFound() {
        Integer id = 99;
        when(userService.findUserById(id)).thenReturn(null);

        ResponseResult<User> result = userController.findUserById(id);

        assertEquals(404, result.getCode());
        assertEquals("未找到用户", result.getMsg());
    }

    @Test
    void testGetList() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.findAllUser()).thenReturn(users);

        ResponseResult<List<User>> result = userController.getList();

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testDelete() {
        Integer id = 1;
        when(userService.deleteUser(id)).thenReturn(true);

        ResponseResult<?> result = userController.delete(id);

        assertEquals(AppHttpCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    void testDeleteNotFound() {
        Integer id = 99;
        when(userService.deleteUser(id)).thenReturn(false);

        ResponseResult<?> result = userController.delete(id);

        assertEquals(404, result.getCode());
        assertEquals("未找到用户", result.getMsg());
    }
}
