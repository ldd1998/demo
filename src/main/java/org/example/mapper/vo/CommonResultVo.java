package org.example.mapper.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.common.result.ResultCode;

/**
 * @author dudycoco
 * @version 1.0.0
 * @ClassName CommonResultVo.java
 * @Description 结果返回公共类
 * @createTime 2022年09月19日 00:16
 */
@Getter
@Setter
@Builder
public class CommonResultVo<T> {
 
    private int code;
    private String message;
    private T data;
 
    protected CommonResultVo() {
    }
 
    protected CommonResultVo(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
 
    /**
     * 成功返回结果
     *
     */
    public static <T> CommonResultVo<T> success() {
        return new CommonResultVo<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }
 
    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResultVo<T> success(T data) {
        return new CommonResultVo<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
 
    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <T> CommonResultVo<T> success(T data, String message) {
        return new CommonResultVo<T>(ResultCode.SUCCESS.getCode(), message, data);
    }
 
    /**
     * 失败返回结果
     * @param resultCode 错误码
     */
    public static <T> CommonResultVo<T> failed(ResultCode resultCode) {
        return new CommonResultVo<T>(resultCode.getCode(), resultCode.getMessage(), null);
    }
 
    /**
     * 失败返回结果
     * @param resultCode 错误码
     * @param message 错误信息
     */
    public static <T> CommonResultVo<T> failed(ResultCode resultCode, String message) {
        return new CommonResultVo<T>(resultCode.getCode(), message, null);
    }
 
    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> CommonResultVo<T> failed(String message) {
        return new CommonResultVo<T>(ResultCode.FAILED.getCode(), message, null);
    }
 
    /**
     * 失败返回结果
     */
    public static <T> CommonResultVo<T> failed() {
        return failed(ResultCode.FAILED);
    }
}