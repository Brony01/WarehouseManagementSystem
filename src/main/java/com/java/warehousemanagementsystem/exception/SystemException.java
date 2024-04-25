package com.java.warehousemanagementsystem.exception;

import com.java.warehousemanagementsystem.enums.AppHttpCodeEnum;

public class SystemException extends RuntimeException{
    private int code;
    private String msg;
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    public SystemException(AppHttpCodeEnum appHttpCodeEnum) {
        super(appHttpCodeEnum.getMsg());
        this.code = appHttpCodeEnum.getCode();
        this.msg = appHttpCodeEnum.getMsg();
    }
}
