package com.z2011.blogapi.Handler;

import com.alibaba.fastjson.JSON;
import com.z2011.blogapi.Service.LoginService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.pojo.SysUser;
import com.z2011.blogapi.utils.UserThreadLocal;
import com.z2011.blogapi.vo.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 * @author z2011
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;

    /**
     * 在controller(Handler)前进行逻辑判断request，返回false则为什么都不返回，为空，这里也不能直接返回，所以写入进response，
     * 为什么使用JSON.toJSONString？这个方法经常用，好用吗？
     * 直接写入会发现中文乱码：{"code":90002,"msg":"???","success":false}，所以需要设置编码：application/json;charset=utf-8设置了返回的格式与编码
     * 为什么需要先判断一下传入的handler是否是controller？在以前其实我们都没动过这个handler，现在肯定就会疑惑了
     * 它的文档是：选择要执行的处理程序，用于类型和或实例评估，可以看出我们
     * 逻辑：1. 首先判断是否是controller路由，不要是静态资源
     * 这里返回true的确放行了，在这里进行的token验证可以查出用户的基本信息，如果能直接传回路由的话就可以不用再查一遍了，那么拦截器怎么传参数给路由呢？
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //是静态资源就放行，可以看出HandlerMethod好像很重要
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }
        //这里有个问题：当请求头没有定义这个属性也就是说没有登录、注册过时，token会是"undefined"字符串，所以不为null或空字符串，所以会进入到jwt格式验证，从而报错
        String token = request.getHeader("Authorization");

        //对拦截了的url进行日志输出
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("threadId：{}",Thread.currentThread().getId());
        log.info("=================request end===========================");

        //service包含了三重token验证：非空、token格式、redis查找，如果正确返回则是已登录并token在有效时间的用户

        SysUser sysUser = loginService.checkToken(token);
        if (sysUser==null){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(Result.FAIL(ErrorCode.NO_LOGIN.getCode(),"未登录")));
            return false;
        }
        UserThreadLocal.put(sysUser);

        log.info("拦截放行，存入user的id="+sysUser.getId());
        return true;
    }


////  拦截处理程序的执行。在 HandlerAdapter 实际调用处理程序之后，但在 DispatcherServlet 呈现视图之前调用
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }
//
//
//  请求处理完成后的回调，即渲染视图后。
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
