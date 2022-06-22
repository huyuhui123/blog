package com.z2011.blogadmin.service;

import com.z2011.blogadmin.dao.popj.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 登录逻辑：相当于访问数据库的user表
 */
@Component
@Slf4j
public class SecurityUserService implements UserDetailsService {
    @Autowired
    private AdminService adminService;


    /**
     * 写完配置类的第一步，用于自定登录逻辑，主要是要在数据库取用户信息
     * 根据用户名查找用户，放行就可登录成功
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("进入自定用户登录认证逻辑");
        Admin admin = adminService.findAdminByUsername(username);
        if (admin==null)
            throw new UsernameNotFoundException("用户名不存在");
        UserDetails user=new User(username,admin.getPassword(),new ArrayList());
        return user;
    }
}