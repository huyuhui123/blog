package com.z2011.blogapi.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.pojo.Article;
import com.z2011.blogapi.vo.params.ArticleParam;
import com.z2011.blogapi.vo.params.PagParam;

public interface ArticleService extends IService<Article> {

    /**
     * 首页获取文章列表
     * @param pagParam
     * @return
     */
    Result getArticleList(PagParam pagParam);

    /**
     * 首页最热文章（简单）
     * @param limit
     * @return
     */
    Result hotArticleList (int limit);

    /**
     * 首页最新文章（同上很像）
     * @param limit
     * @return
     */
    Result newArticleList(int limit);

    /**
     * 文章归档（根据年、月进行分组）
     * @return
     */
    Result ListArchives();

    /**
     * 文章详情
     * 根据articleId查找vo，涉及到的表：article、articleBody、ms_category、ms_article_tag
     * 想查找 articleBody先pojo，然后
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    /**
     * 提交文章
     * @param articleParam
     * @return
     */
    Result publishArticle(ArticleParam articleParam);
}
