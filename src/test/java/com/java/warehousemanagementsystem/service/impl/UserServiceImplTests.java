//package com.java.warehousemanagementsystem.service.impl;
//
//import com.java.warehousemanagementsystem.pojo.User;
//import com.java.warehousemanagementsystem.mapper.UserMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceImplTests {
//
//    @Mock
//    private UserMapper userMapper;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Test
//    void testRegisterSuccess() {
//        String username = "newUser";
//        String password = "password123";
//        String confirmedPassword = "password123";
//
//        when(userMapper.selectCount(any())).thenReturn(0L);
//        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
//
//        assertDoesNotThrow(() -> userService.register(username, password, confirmedPassword));
//        verify(userMapper).insert(any(User.class));
//    }
//
//    @Test
//    void testRegisterUserExists() {
//        String username = "existingUser";
//        String password = "password123";
//        String confirmedPassword = "password123";
//
//        when(userMapper.selectCount(any())).thenReturn(1L);
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.register(username, password, confirmedPassword));
//        assertEquals("用户已存在", exception.getMessage());
//    }
//
//    @Test
//    void testRegisterNullUsername() {
//        String username = null;
//        String password = "password123";
//        String confirmedPassword = "password123";
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.register(username, password, confirmedPassword));
//        assertEquals("用户名不能为空", exception.getMessage());
//
//        verify(userMapper, never()).insert(any());
//    }
//
//    @Test
//    void testRegisterLongUsername() {
//        String username = "a".repeat(101);
//        String password = "password123";
//        String confirmedPassword = "password123";
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.register(username, password, confirmedPassword));
//        assertEquals("用户名过长", exception.getMessage());
//
//        verify(userMapper, never()).insert(any());
//    }
//
//    @Test
//    void testRegisterNullPassword() {
//        String username = "newUser";
//        String password = null;
//        String confirmedPassword = "password123";
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.register(username, password, confirmedPassword));
//        assertEquals("密码不能为空", exception.getMessage());
//
//        verify(userMapper, never()).insert(any());
//    }
//
//    @Test
//    void testRegisterLongPassword() {
//        String username = "newUser";
//        String password = "a".repeat(101);
//        String confirmedPassword = "password123";
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.register(username, password, confirmedPassword));
//        assertEquals("密码过长", exception.getMessage());
//
//        verify(userMapper, never()).insert(any());
//    }
//
//    @Test
//    void testRegisterDifferentPasswords() {
//        String username = "newUser";
//        String password = "password123";
//        String confirmedPassword = "differentPassword";
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.register(username, password, confirmedPassword));
//        assertEquals("密码不一致", exception.getMessage());
//
//        verify(userMapper, never()).insert(any());
//    }
//
//    @Test
//    void testUpdateUserSuccess() {
//        int id = 1;
//        String username = "updatedUser";
//        String password = "newPassword";
//        String confirmedPassword = "newPassword";
//
//        User user = new User(id, "user", "password");
//        when(userMapper.selectById(id)).thenReturn(user);
//        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
//
//        assertTrue(userService.updateUser(id, username, password, confirmedPassword));
//
//        assertEquals(username, user.getUsername());
//        assertEquals("encodedPassword", user.getPassword());
//
//        verify(userMapper).updateById(user);
//    }
//
//
//    @Test
//    void testUpdateUserNotFound() {
//        int id = 1;
//        String username = "updatedUser";
//        String password = "newPassword";
//        String confirmedPassword = "newPassword";
//
//        when(userMapper.selectById(id)).thenReturn(null);
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.updateUser(id, username, password, confirmedPassword));
//        assertEquals("用户不存在", exception.getMessage());
//    }
//
//    @Test
//    void testDeleteUser() {
//        Integer id = 1;
//        when(userMapper.deleteById(id)).thenReturn(1);
//
//        assertTrue(userService.deleteUser(id));
//    }
//
//    // Add more tests for findUserById, findAllUser, and handling exceptions
//    @Test
//    void testFindUserById() {
//        Integer id = 1;
//        User user = new User(id, "user", "password");
//        when(userMapper.selectById(id)).thenReturn(user);
//
//        assertEquals(user, userService.findUserById(id));
//    }
//
//    @Test
//    void testFindUserByIdNullId() {
//        Integer id = null;
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.findUserById(id));
//        assertEquals("用户id不能为空", exception.getMessage());
//    }
//
//    @Test
//    void testFindAllUser() {
//        List<User> users = List.of(new User(1, "user1", "password1"),
//                new User(2, "user2", "password2"));
//        when(userMapper.selectList(null)).thenReturn(users);
//
//        assertEquals(users, userService.findAllUser());
//    }
//
//    @Test
//    void testDeleteUserNullId() {
//        Integer id = null;
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.deleteUser(id));
//        assertEquals("用户id不能为空", exception.getMessage());
//    }
//
//    @Test
//    void testUpdateUser() {
//        int id = 1;
//
//        String username = "updatedUser";
//        String password = "newPassword";
//        String confirmedPassword = "newPassword";
//
//        User user = new User(id, "user", "password");
//        when(userMapper.selectById(id)).thenReturn(user);
//        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
//
//        assertTrue(userService.updateUser(id, username, password, confirmedPassword));
//
//        assertEquals(username, user.getUsername());
//        assertEquals("encodedPassword", user.getPassword());
//
//        verify(userMapper).updateById(user);
//    }
//
//    @Test
//    void testUpdateUserNullId() {
//        int id = 1;
//        String username = "updatedUser";
//        String password = "newPassword";
//        String confirmedPassword = "newPassword";
//
//        when(userMapper.selectById(id)).thenReturn(null);
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.updateUser(id, username, password, confirmedPassword));
//        assertEquals("用户不存在", exception.getMessage());
//
//        verify(userMapper, never()).updateById(any());
//    }
//
//    @Test
//    void testUpdateUserNullUsername() {
//        int id = 1;
//        String username = null;
//        String password = "newPassword";
//        String confirmedPassword = "newPassword";
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.updateUser(id, username, password, confirmedPassword));
//        assertEquals("用户名不能为空", exception.getMessage());
//
//        verify(userMapper, never()).updateById(any());
//    }
//
//    @Test
//    void testUpdateUserLongUsername() {
//        int id = 1;
//        String username = "a".repeat(101);
//        String password = "newPassword";
//        String confirmedPassword = "newPassword";
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.updateUser(id, username, password, confirmedPassword));
//        assertEquals("用户名过长", exception.getMessage());
//
//        verify(userMapper, never()).updateById(any());
//    }
//
//    @Test
//    void testUpdateUserNullPassword() {
//        int id = 1;
//        String username = "updatedUser";
//        String password = null;
//        String confirmedPassword = "newPassword";
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.updateUser(id, username, password, confirmedPassword));
//        assertEquals("密码不能为空", exception.getMessage());
//
//        verify(userMapper, never()).updateById(any());
//    }
//
//    @Test
//    void testUpdateUserLongPassword() {
//        int id = 1;
//        String username = "updatedUser";
//        String password = "a".repeat(101);
//        String confirmedPassword = "newPassword";
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.updateUser(id, username, password, confirmedPassword));
//        assertEquals("密码过长", exception.getMessage());
//
//        verify(userMapper, never()).updateById(any());
//    }
//
//    @Test
//    void testUpdateUserDifferentPasswords() {
//        int id = 1;
//        String username = "updatedUser";
//        String password = "newPassword";
//        String confirmedPassword = "differentPassword";
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () ->
//                userService.updateUser(id, username, password, confirmedPassword));
//        assertEquals("密码不一致", exception.getMessage());
//
//        verify(userMapper, never()).updateById(any());
//    }
//}
