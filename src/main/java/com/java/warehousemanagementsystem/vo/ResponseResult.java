package com.java.warehousemanagementsystem.vo;

import com.java.warehousemanagementsystem.enums.AppHttpCodeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 使用私有构造函数配合工厂方法
public class ResponseResult<T> implements Serializable {
    private final Integer code;
    private final String msg;
    private final T data;

    // 成功响应，无数据返回
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(AppHttpCodeEnum.SUCCESS.getCode(), AppHttpCodeEnum.SUCCESS.getMessage(), null);
    }

    // 成功响应，有数据返回
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(AppHttpCodeEnum.SUCCESS.getCode(), AppHttpCodeEnum.SUCCESS.getMessage(), data);
    }

    // 成功响应，自定义消息
    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(AppHttpCodeEnum.SUCCESS.getCode(), message, data);
    }

    // 失败响应，指定错误代码和消息
    public static <T> ResponseResult<T> failure(int code, String msg) {
        return new ResponseResult<>(code, msg, null);
    }

    // 失败响应，使用枚举类型提供错误代码和消息
    public static <T> ResponseResult<T> failure(AppHttpCodeEnum errorCode) {
        return new ResponseResult<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    // 失败响应，自定义错误消息
    public static <T> ResponseResult<T> failure(AppHttpCodeEnum errorCode, String errorMessage) {
        return new ResponseResult<>(errorCode.getCode(), errorMessage, null);
    }


}
