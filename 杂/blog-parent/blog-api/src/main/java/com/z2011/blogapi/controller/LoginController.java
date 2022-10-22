package com.z2011.blogapi.controller;

import com.z2011.blogapi.Service.LoginService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * 登录成功返回token
     * @param loginParam
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }
}
