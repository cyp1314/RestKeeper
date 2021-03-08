package com.restkeeper.response.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: 拦截异常并转换成 自定义ExceptionResponse
 * @date 2021-03-08 16:03
 */
@RestControllerAdvice
public class RestKeeperExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object Exception(Exception ex) {
        ExceptionResponse response =new ExceptionResponse(ex.getMessage());
        return response;
    }
}
