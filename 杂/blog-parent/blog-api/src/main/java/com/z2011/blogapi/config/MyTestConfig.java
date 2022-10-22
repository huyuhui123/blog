package com.z2011.blogapi.config;

import com.z2011.blogapi.controller.ArticleController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;


@Configuration
public class MyTestConfig {
    /**
     * 我在这里用@Bean注解注册了一个ArticleController，程序应该会先根据配置文件走，然后扫描，再去找到controller，那这总有一个注册失败
     *
     * @return
     */
    // @Bean
    // @Lazy
    // public ArticleController articleController(){
    //     return new ArticleController();
    // }
}