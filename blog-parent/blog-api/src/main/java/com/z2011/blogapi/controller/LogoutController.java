package com.z2011.blogapi.controller;

import com.z2011.blogapi.Service.LoginService;
import com.z2011.blogapi.dao.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class LogoutController {
    @Autowired
    private LoginService loginService;

    @GetMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String token){
       return loginService.logout(token);
    }
}
