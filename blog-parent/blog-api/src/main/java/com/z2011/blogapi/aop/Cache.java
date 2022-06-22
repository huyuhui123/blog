package com.z2011.blogapi.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Cache {
    String name() default "";
    long expire() default 60*60*1000;//一小时

}
