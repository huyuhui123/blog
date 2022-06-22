package com.z2011.blogadmin.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private boolean success;
    private Integer code;
    private String msg;
    private Object data;

    public static Result SUCCESS(Object data) {
        return new Result(true,200,"success",data);
    }
    public static Result FAIL(Integer code,String msg) {
        return new Result(false,code,msg,null);
    }
}