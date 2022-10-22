package com.z2011.blogapi.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.pojo.SysUser;
import com.z2011.blogapi.vo.SysUserVo;

public interface SysUserService  extends IService<SysUser> {
    /**
     * 通过帐号密码找到登录所需要的user信息
     * @param account
     * @param pwd
     * @return
     */
    SysUser selectLoginUser(String account, String pwd);

    /**
     * 通过token去redis查，为空判断、没有找到判断
     * 查到了就用JSON.parseObject(json, Model.class)转换成SysUser返回
     * 没查到
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    /**
     * 评论路由中需要通过评论人Id获得评论人信息
     * @param authorId
     * @return
     */
    SysUserVo findUserVoById(Long authorId);
}
