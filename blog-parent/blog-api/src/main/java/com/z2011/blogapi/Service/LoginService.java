package com.z2011.blogapi.Service;

import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.pojo.SysUser;
import com.z2011.blogapi.vo.params.LoginParam;

public interface LoginService {
    /**
     * 登录
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    /**
     * /user/currentUser：传入token获取用户信息
     * 逻辑：非空、token格式、redis查找三重检验，然后返回用户信息
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册逻辑：非空判断、数据库查询、
     * 对于注册逻辑，我总觉得我做的不太放心，缺了哪里？
     * 总的来说注册就是把用户的数据存进数据库，对参数判断、在数据库查找有无重复、没有重复插入数据库返回token
     * 要不要格式判断？在哪里做？应该返回什么？
     * 只有三个字段，其他字段怎么办？
     * 我先完成这个接口
     * 这里有点问题（数据验证与自定义异常）：无论执行到哪，都是返回Result，那还不如自定义异常通用一些，
     * 这里很明显太依赖与service返回result了，如果是service调用service的话，在过程中报错还要一层一层返回Result
     *
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);
}
