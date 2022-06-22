package com.z2011.blogapi.controller;

import com.z2011.blogapi.Service.LoginService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    public LoginService loginService;
    @PostMapping("/register")
    public Result register(@RequestBody LoginParam loginParam){
        return loginService.register(loginParam);

    }
}
