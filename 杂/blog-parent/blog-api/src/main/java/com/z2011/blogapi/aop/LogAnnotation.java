package com.z2011.blogapi.aop;

import java.lang.annotation.*;

/**
 * 创建注解，用于定义切入点
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LogAnnotation {
    String model() default "";

    String operator() default "";


}
