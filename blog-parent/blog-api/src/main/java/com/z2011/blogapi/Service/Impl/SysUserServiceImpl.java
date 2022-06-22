package com.z2011.blogapi.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.z2011.blogapi.Service.LoginService;
import com.z2011.blogapi.Service.SysUserService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.mapper.SysUserMapper;
import com.z2011.blogapi.dao.pojo.SysUser;
import com.z2011.blogapi.vo.SysUserVo;
import com.z2011.blogapi.vo.enums.ErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private LoginService loginService;

    @Override
    public SysUser selectLoginUser(String account, String pwd) {
        /*select * from ms_sys_user where account='' and `password`=''
        查询并返回一条数据*/
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SysUser::getAccount,account);
        userWrapper.eq(SysUser::getPassword,pwd);
        userWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        return userMapper.selectOne(userWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        //在loginService里判断token并在redis返回user
        SysUser sysUser1 = loginService.checkToken(token);
        if(sysUser1==null) {
            return Result.FAIL(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        return Result.SUCCESS(sysUser1);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper();
        wrapper.eq(SysUser::getAccount,account);
        wrapper.select(SysUser::getAccount);
        wrapper.last("limit 1");
        return  userMapper.selectOne(wrapper);
    }

    @Override
    public SysUserVo findUserVoById(Long authorId) {
//        select id,avatar,nickname from ms_sysuser where id={commnet.authorId}
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SysUser::getId,authorId);
        userWrapper.select(SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        userWrapper.last("limit 1");
        SysUser sysUser = userMapper.selectOne(userWrapper);
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(sysUser,sysUserVo);
        sysUserVo.setId(sysUser.getId().toString());
        return sysUserVo;
    }

}
