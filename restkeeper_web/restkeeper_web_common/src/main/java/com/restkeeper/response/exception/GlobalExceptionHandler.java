package com.restkeeper.response.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: 异常拦截
 * @date 2021-03-08 15:59
 */
//@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(java.lang.Exception.class)
    public Object Exception(Exception exception){
        Map<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("errCode", "00000");
        errorMap.put("errMessage", exception.getMessage());
        return errorMap;
    }
}
