package com.z2011.blogapi.controller;

import com.z2011.blogapi.dao.pojo.SysUser;
import com.z2011.blogapi.dao.pojo.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
    /**
     * 这里先不看如何根据controller来匹配路由，先看参数问题
     * 最基本的问题：最基本的controller无论有没有参数，只要只匹配到controller名只有一个，那就是它，也不会报错，也就是说对参数无限定，可为null
     * 这样可不行，我不想在service层加非空判断，
     * @return
     */
    @PostMapping("t")
    public String test(@RequestBody String name, String password){

        return name;
    }
    @PostMapping
    public String test2(@RequestBody User user){

        return user.getName();
    }

}
