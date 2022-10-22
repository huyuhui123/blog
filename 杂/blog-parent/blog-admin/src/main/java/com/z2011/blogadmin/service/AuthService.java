package com.z2011.blogadmin.service;

import com.z2011.blogadmin.dao.popj.Admin;
import com.z2011.blogadmin.dao.popj.Permission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Permissions;
import java.util.List;

/**
 * 写完配置类的第二步
 * 鉴权逻辑
 * 登录逻辑负责一些路由，鉴权逻辑负责另一些路由
 */
@Service
@Slf4j
public class AuthService {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AdminService adminService;
    public boolean  auth(HttpServletRequest request, Authentication authentication){
        String requestURL = request.getRequestURI();
        log.info("request url:{}",requestURL);
        //select * from ms_permission where path={requestURL} : permission
        Object principal = authentication.getPrincipal();//存储着用户信息
        //判断是否为空或为匿名用户
        if (principal==null || "anonymousUser".equals(principal)){
            return false;
        }
        UserDetails userDetails= (UserDetails) principal;
        String username = userDetails.getUsername();
        //根据用户名和路由可以判断是否能行，因为这不存在角色的定义，所以就用这种方式
        // 关键在于这里可以有很多种写法，复杂的简单化，先写下来再说，不然之后又看不懂，又难维护
        //select * from ms_
        Admin admin = adminService.findAdminByUsername(username);
        if(admin==null){
            return false;
        }
        if (admin.getId()==1){
            return true;
        }
        Long adminId = admin.getId();
        List<Permission> permissions = adminService.findPermissionByAdminId(adminId);
        requestURL=StringUtils.split(requestURL,'?')[0];
        "123".toCharArray();
        String replace = "123".replace("2", "");
        "1230".substring('5',2);
        for (Permission permission : permissions) {
            if (requestURL.equals(permission.getPath())){
                return true;
            }
        }

        return false;
    }
}