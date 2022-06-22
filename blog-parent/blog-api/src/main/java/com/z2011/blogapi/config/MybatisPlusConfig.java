package com.z2011.blogapi.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.z2011.blogapi.dao.pojo.Article;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@MapperScan("com.z2011.blogapi.dao.mapper")
@Configuration
public class MybatisPlusConfig {

    /**
     * MybatisPlus分页配置
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisConfiguration(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
    @Lazy
    @Bean
    public Article getArticle(){
        return new Article();
    }
}
