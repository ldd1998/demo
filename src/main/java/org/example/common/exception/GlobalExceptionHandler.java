package org.example.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 处理其他异常，例如通用的RuntimeException
    @ExceptionHandler(Exception.class)
    public void handleOtherExceptions(Exception ex) throws Exception {
        throw ex;
    }
}
