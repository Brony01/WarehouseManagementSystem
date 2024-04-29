package com.java.warehousemanagementsystem.enums;

import lombok.Getter;

@Getter
public enum AppHttpCodeEnum {
    // 成功响应
    SUCCESS(200, "操作成功"),
    CREATED(201, "资源创建成功"),
    ACCEPTED(202, "请求已接受，但处理尚未完成"),
    NO_CONTENT(204, "操作成功，但无内容返回"),

    // 重定向
    MOVED_PERMANENTLY(301, "资源永久移动"),
    FOUND(302, "资源临时移动"),
    SEE_OTHER(303, "请参见其他地址"),
    NOT_MODIFIED(304, "资源未修改"),

    // 客户端错误
    BAD_REQUEST(400, "请求错误，请修正请求"),
    UNAUTHORIZED(401, "需要认证信息"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源未找到"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),
    NOT_ACCEPTABLE(406, "请求的内容类型无法接受"),
    CONFLICT(409, "请求冲突"),
    GONE(410, "资源已被永久删除"),
    LENGTH_REQUIRED(411, "未指定内容长度"),
    PRECONDITION_FAILED(412, "前置条件失败（如时间戳检查失败）"),
    PAYLOAD_TOO_LARGE(413, "请求体过大"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    TOO_MANY_REQUESTS(429, "请求过多"),

    // 服务器错误
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),
    NOT_IMPLEMENTED(501, "服务器不支持请求的功能，无法完成请求"),
    BAD_GATEWAY(502, "无效的响应"),
    SERVICE_UNAVAILABLE(503, "服务器暂时不可用（可能是过载或维护）"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    HTTP_VERSION_NOT_SUPPORTED(505, "不支持的HTTP版本");

    private final int code;
    private final String message;

    AppHttpCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
