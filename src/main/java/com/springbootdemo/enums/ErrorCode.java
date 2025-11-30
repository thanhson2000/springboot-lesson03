package com.springbootdemo.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {

    INVALID_KEY(4001,"無效key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(4002,"用戶已存在", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(4003,"用戶不存在", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(4004,"未經鑑定的", HttpStatus.UNAUTHORIZED)
    ;


    private int code;
    private String msg;
    private HttpStatusCode statusCode;
    ErrorCode(int code, String msg, HttpStatusCode statusCode) {
        this.code = code;
        this.msg = msg;
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
