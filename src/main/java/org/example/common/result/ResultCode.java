package org.example.common.result;

import lombok.Getter;
import lombok.Setter;
 
/**
 * @author dudycoco
 * @version 1.0.0
 * @ClassName ResultCode.java
 * @Description 返回值code
 * @createTime 2022年09月19日 00:22
 */
 
public enum ResultCode {
    SUCCESS(0, "操作成功"),
    FAILED(-1, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");
 
    @Setter
    @Getter
    private int code;
 
    @Setter
    @Getter
    private String message;
 
    private ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
 
 
}