package com.z2011.blogapi.aop;


import com.alibaba.fastjson.JSON;
import com.z2011.blogapi.dao.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * 统一缓存切面定义
 * 在调用方法前去redis看是否有缓存，有就直接返回，没有再调用方法然后通过key为注解的属性名，values为结果放进缓存里
 * 日志我先不做，有参数对缓存没影响吧，错了，传入参数会变，这也影响该缓存什么
 * 统一：比如我这里先给hotArticles加上了，它没有参数、完成基本任务就可以
 * 有参数比如像articleById（long id），想不出来去试验了一下，很有意思无论传什么都不加入判定一直返回同样的，这样就需要通过参数名来改变key
 *
 */
@Component
@Aspect
@Slf4j
public class CatcheLogAspect {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Pointcut("@annotation(com.z2011.blogapi.aop.Cache)")
    public void pt() {
    }

    /**
     * 先直接输出看一看,ArticleList( @RequestBody PagParam pagParam)很明显输出的就是PagParam，把他string化+方法名不就行了
     可以直接数组string化很好
     参数不为零，以参数+注解的name属性值作为key进行查找
     查找到了则直接返回
     没查找到就执行方法，把结果放进缓存里
     参数为零，以注解的name属性值作为key进行查找
     查找到了则直接返回
     没查找到就执行方法，把结果放进缓存里
     *
     * @param jp
     * @return
     * @throws Throwable
     */
    @Around("pt()")
    public Object LogAround(ProceedingJoinPoint jp) throws Throwable {

        //方法获取先不管，先完成日志、redis的name,虽然写的粗糙了点，但也行，下一个
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        //获取method上被注解的name属性值,注解name属性值+类名字+方法名字+加密了参数的md5
        Cache annotation = method.getAnnotation(Cache.class);
        String ClassName = jp.getTarget().getClass().getName();
        String MethodName=method.getName();

        String name = annotation.name();//name这里设置的是ArrayList
        long expire = annotation.expire();
        Object[] args = jp.getArgs();
        String ParamsName="";

        for (Object arg : args) {
            if (arg!=null){
                ParamsName=ParamsName+JSON.toJSONString(arg);
            }
        }
        //空字符串这里是false，但哪怕是空格都是true
        //这里有问题，就算参数为空不报错，但这里也不能
        if(StringUtils.isNotEmpty(ParamsName)){
            ParamsName=DigestUtils.md5Hex(ParamsName);
        }
        //在redis存储的方式是
        name=name+"::"+ClassName+"::"+MethodName+"::"+ParamsName;
        String ValuesString = redisTemplate.boundValueOps(name).get();
        if(StringUtils.isNotEmpty(ValuesString)){
            log.info("走缓存了~，东西是："+name);
            return JSON.parseObject(ValuesString, Result.class);
        }else {
            Object proceed = jp.proceed();
            log.info("没走缓存~，没走缓存的是："+name);
            //
            redisTemplate.boundValueOps(name).set(JSON.toJSONString(proceed), Duration.ofMillis(expire));//60000毫秒，就是一分钟
            redisTemplate.opsForValue();
            return proceed;
        }

    }
}
