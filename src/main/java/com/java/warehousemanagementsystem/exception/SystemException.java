package com.java.warehousemanagementsystem.exception;

import com.java.warehousemanagementsystem.enums.AppHttpCodeEnum;
import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {
    private final int code;
    private final String msg;

    public SystemException(AppHttpCodeEnum appHttpCodeEnum) {
        super(appHttpCodeEnum.getMessage());
        this.code = appHttpCodeEnum.getCode();
        this.msg = appHttpCodeEnum.getMessage();
    }
}
