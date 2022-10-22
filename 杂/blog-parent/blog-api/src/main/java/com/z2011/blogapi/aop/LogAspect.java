package com.z2011.blogapi.aop;

import com.z2011.blogapi.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 如同面向对象编程一样，面向切面编程最先定义的就是切面，定义切面要怎么用？
 * 切面编程的基础知识：切点是什么？
 * 切面是包含aop操作的逻辑代码相当于构造器一般的存在，切点是具体位置用到切面里存储的逻辑处理的标记点
 *
 * @Pointcu：是标记点的定义，带有在哪用逻辑的标记信息，它里面的东西很少，就是value属性 为什么string类型里可以用@annotation？@annotation到底是什么？
 * 注解的创建：元注解、注解的基础
 * 切面的创建：通知与切点
 * 切点通过创建注解来定位，通知经过实验知道返回类型不给他返回的话就会被拦截到了，但这不重要关键在于怎么用反射]
 * JoinPoint：提供对连接点可用状态和有关它的静态信息的反射访问。
 * Signature:连接点处的签名
 */
@Component
@Aspect
@Slf4j
public class LogAspect {


    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.z2011.blogapi.aop.LogAnnotation)")
    public void pt() {
    }

    /**
     * 定义通知：环绕通知
     * 关键在于对目标的方法
     */
    @Around("pt()")
    public Object LogAround(ProceedingJoinPoint jp) throws Throwable {
//        //获得方法对象
//        MethodSignature signature = (MethodSignature) jp.getSignature();
//        Method method = signature.getMethod();
//        //获取方法上的注解
//        LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
//        System.out.println("方法上LogAnnotation类型注解的model属性值：" + annotation.model());
//        //方法名和类名
//        //这是不是说明可以获取被切面方法的类名字，但不能动态获取这个类对象，切面可以做到对方法被调用时的切面处理，那创建的实例可以切面吗？
//        System.out.println("被切面的className：" + jp.getTarget().getClass().getName());//com.example.springcontrollexception.controller.commodityController
//        System.out.println("被切面的方法名：" + method.getName());//getCommodityList
//        //获得传入参数
//        for (Object arg : jp.getArgs()) {
//            System.out.println("被切面方法传入的参数：" + arg);//6
//        }
//        //
////        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        long beginTime = System.currentTimeMillis();
        Object proceed = jp.proceed();
        long time = System.currentTimeMillis()-beginTime;
        recordLog(jp,time);
        return proceed;


    }

    /**
     * - 日志注解传入的两个属性值
     * - 日志请求的类与方法名
     * - 日志方法的参数
     * - 日志请求的ip
     * - 日志执行时间
     * 我这里没有对方法的参数进行toString
     * log.info对时间自动转换
     * @param jp
     * @param time
     */
    private void recordLog(ProceedingJoinPoint jp, long time) {
        //获得方法对象
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        log.info("=====================log start================================");
//        日志注解传入的两个属性值
        LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
        log.info("model:{}",annotation.model());
        log.info("operator：{}",annotation.operator());
//        日志请求的类与方法名
        String className = jp.getTarget().getClass().getName();
        String methodName = method.getName();
        log.info("className:{}",className+"."+"methodName."+methodName+"()");
//        日志请求的参数
        for (Object arg : jp.getArgs()) {
            log.info("方法参数：{}",arg);
        }
//        日志请求的ip
        log.info("ip:{}",IpUtils.getIpAddr());
//        日志执行时间
        log.info("excute time : {} ms",time);
        log.info("=====================log end================================");

    }

}
