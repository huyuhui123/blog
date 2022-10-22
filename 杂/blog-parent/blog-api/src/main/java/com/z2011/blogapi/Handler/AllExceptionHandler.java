package com.z2011.blogapi.Handler;

import com.z2011.blogapi.dao.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AllExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception e){
        e.printStackTrace();
        return Result.FAIL(-99,"全局错误");
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result MothodException(Exception e){
        e.printStackTrace();
        return Result.FAIL(-98,"参数错误");
    }


}
