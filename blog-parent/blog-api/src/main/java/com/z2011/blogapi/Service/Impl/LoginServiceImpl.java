package com.z2011.blogapi.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.z2011.blogapi.Service.LoginService;
import com.z2011.blogapi.Service.SysUserService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.pojo.SysUser;
import com.z2011.blogapi.utils.JWTUtils;
import com.z2011.blogapi.vo.enums.ErrorCode;
import com.z2011.blogapi.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    //加密盐用于加密
    private static final String slat = "mszlu!@#";
    @Autowired
    private SysUserService userService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 登录逻辑：为null判断（非注解）、密码加密、进行查询、jwt返回token并把用户信息记录在redis里
     * 为空判断：用的是apache的工具包的isBlank，和spring的isEmpty有什么区别？isEmpty只是判断了null和字符串长度，
     * 但isBlank会先判断长度，没有长度直接判为true，有长度但全为空格符号也会判断为true，比isEmpty多一个是否全为空格符的判断
     * md5加密：没什么好说的
     * 进行查询：他这里把查询的语句放到userService里去了，我这里随意，不过getOne有个坑，如果返回数据不止一条会报错，还要加个参数
     * jwt
     * @param loginParam
     * @return
     */
    @Override
    public Result login(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        //判断空
        if (StringUtils.isBlank(account) | StringUtils.isBlank(password)) {
            return Result.FAIL(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //密码md5加密，设置好的盐防止解密
        String pwd = DigestUtils.md5Hex(password + slat);
        //查找交给userService去做
        SysUser sysUser = userService.selectLoginUser(account, pwd);
        if (sysUser==null) {
            return Result.FAIL(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //jwt通过id制作token
        String token = JWTUtils.createToken(sysUser.getId());
        //token做键，firstJson转换user信息做值传入redis
        redisTemplate.boundValueOps("TOKEN_"+token).set(JSON.toJSONString(sysUser),1, TimeUnit.DAYS);


        return Result.SUCCESS(token);
    }

    @Override
    public SysUser checkToken(String token) {
        //为空判断
        if(StringUtils.isBlank(token) || token.equals("undefined"))
            return null;
        //jwt检验，为什么要进行赋值而不是直接写在if里面？
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap==null)
            return null;
        //进入redis找token
        String userToken = redisTemplate.boundValueOps("TOKEN_" + token).get();
        if (StringUtils.isBlank(userToken))
            return null;
        return JSON.parseObject(userToken,SysUser.class);
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.SUCCESS(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        //非空判断、数据库查找、数据库保存、生成token存入redis（重复代码）
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        //判空
        if (StringUtils.isBlank(account)||StringUtils.isBlank(password)||StringUtils.isBlank(nickname))
            return Result.FAIL(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        //数据库看有没有，没有就注册，有就报错
        SysUser sysUser = userService.findUserByAccount(account);
        if (sysUser!=null)
            return Result.FAIL(ErrorCode.ACCOUNT_ERROR.getCode(),"账户已经被注册了");//这里msg自己写的
        //用户名不存在，插入数据库，存入vo到redis、返回token
        //我这里先不管存入的数据的其他字段的默认值
        //vo我先只放最基础的
        //他这里自己定义了默认值存入数据库中
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        userService.save(sysUser);
        //生成token存入redis，他这里没用vo，我一直以为要
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.boundValueOps("TOKEN_"+token).set(JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.SUCCESS(token);
    }
}
