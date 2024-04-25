package com.java.warehousemanagementsystem.handler;


import com.java.warehousemanagementsystem.enums.AppHttpCodeEnum;
import com.java.warehousemanagementsystem.exception.SystemException;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e) {
        log.error("出现了异常! {}", e);
        return ResponseResult.failure(e.getCode(), e.getMsg());
    }
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error("出现了异常! {}", e);
        return ResponseResult.failure(AppHttpCodeEnum.BAD_GATEWAY.getCode(), e.getMessage());
    }
}
