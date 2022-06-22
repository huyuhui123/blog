package com.z2011.blogapi.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.z2011.blogapi.dao.mapper.ArticleMapper;
import com.z2011.blogapi.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    @Async("taskExecutor")
    public void updateViewContents(ArticleMapper articleMapper, Article article){
        //修改阅读次数，这里有点特殊所以加一个wrapper，不然直接根据id就可以修改了
//        update article set ViewCounts=ViewCounts+1 where   id={article.getId()} and ViewCounts=ViewCounts
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(Article::getId,article.getId());
        updateWrapper.eq(Article::getViewCounts,article.getViewCounts());
        //传入只设置了观看次数的对象来防止update重复元素，毕竟他会对null的属性忽略
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(article.getViewCounts()+1);
        articleMapper.update(articleUpdate,updateWrapper);
//        System.out.println("修改线程Id："+Thread.currentThread().getId());
//        try {
//            Thread.sleep(5000);
//            System.out.println("修改线程结束");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
