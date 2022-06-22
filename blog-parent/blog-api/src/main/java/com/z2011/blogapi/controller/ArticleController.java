package com.z2011.blogapi.controller;

import com.z2011.blogapi.Service.ArticleService;
import com.z2011.blogapi.aop.Cache;
import com.z2011.blogapi.aop.LogAnnotation;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.vo.params.ArticleParam;
import com.z2011.blogapi.vo.params.PagParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/articles")
@Validated
@Slf4j
@Api
public class ArticleController {
    @Autowired
    @Resource
    private ArticleService articleService;

    static{
        log.info("静态方法被执行，ArticleController被加载了");
    }
    /**
     * 分页文章
     * 添加aop日志
     * @param pagParam
     * @return
     */
    @ApiOperation("获取文章列表")
    @PostMapping
    @LogAnnotation(model="文章",operator="获取文章列表")
    // @Cache(name="ArticleList")
    public Result ArticleList( @RequestBody  PagParam pagParam){
       return articleService.getArticleList(pagParam);
    }

    /**
     * 最热文章
     * @return
     */
    @PostMapping("/hot")
    public Result hotArticleList(){
        int limit =5;
        return articleService.hotArticleList(limit);
    }
    /**
     * 最热文章
     * @return
     */
    @PostMapping("/new")
    @Cache(name="hotArticle")
    public Result newArticleList(){
        int limit =5;
        return articleService.newArticleList(limit);
    }
    /**
     * 文章归档
     * @return
     */
    @PostMapping("/listArchives")
    public Result getListArchives(){
        return articleService.ListArchives();
    }

    /**
     * 文章详情
     * @param articleId
     * @return
     */
    @PostMapping("/view/{articleId}")
    public Result findArticleById(@PathVariable Long articleId){
        return articleService.findArticleById(articleId);
    }

    /**
     * 写文章
     * @param articleParam
     * @return
     */
    @PostMapping("publish")
    public Result publishArticle(@RequestBody ArticleParam articleParam){
        return articleService.publishArticle(articleParam);

    }



}
