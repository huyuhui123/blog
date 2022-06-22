package com.z2011.blogapi.utils;

import com.z2011.blogapi.dao.pojo.SysUser;

/**
 * ThreadLocal：存储线程局部变量
 */
public class UserThreadLocal {

    private final static ThreadLocal<SysUser> LOCAL = new ThreadLocal();

    /**构造方法私有化，单例的第一步*/
    private UserThreadLocal(){}
    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }
    public static SysUser get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }



}
