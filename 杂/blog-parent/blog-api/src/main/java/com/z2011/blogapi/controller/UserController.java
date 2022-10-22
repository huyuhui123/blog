package com.z2011.blogapi.controller;

import com.z2011.blogapi.Service.SysUserService;
import com.z2011.blogapi.dao.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private SysUserService userService;

    /**
     * 上一个接口通过帐号密码获取到了token，浏览器会存储并带着token来访问
     * @return
     */
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){

        return userService.findUserByToken(token);
    }
}
