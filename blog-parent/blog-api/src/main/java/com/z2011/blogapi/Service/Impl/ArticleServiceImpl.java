package com.z2011.blogapi.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.z2011.blogapi.Service.ArticleService;
import com.z2011.blogapi.Service.CategoryService;
import com.z2011.blogapi.Service.SysUserService;
import com.z2011.blogapi.Service.TagService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.dos.Archives;
import com.z2011.blogapi.dao.mapper.ArticleBodyMapper;
import com.z2011.blogapi.dao.mapper.ArticleMapper;
import com.z2011.blogapi.dao.mapper.ArticleTagMapper;
import com.z2011.blogapi.dao.pojo.Article;
import com.z2011.blogapi.dao.pojo.ArticleBody;
import com.z2011.blogapi.dao.pojo.ArticleTag;
import com.z2011.blogapi.utils.UserThreadLocal;
import com.z2011.blogapi.vo.ArticleBodyVo;
import com.z2011.blogapi.vo.ArticleVo;
import com.z2011.blogapi.vo.TagVo;
import com.z2011.blogapi.vo.params.ArticleParam;
import com.z2011.blogapi.vo.params.PagParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SysUserService userService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleBodyMapper bodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    /**
     * 分页、
     * @param pagParam
     * @return
     */
    @Override
    public Result getArticleList(PagParam pagParam) {
        IPage page = new Page(pagParam.getPage(), pagParam.getPageSize());
        IPage<Article> articleList=articleMapper.articleList(page,pagParam.getCategoryId(),pagParam.getTagId(),pagParam.getYear(),pagParam.getMonth());
        return Result.SUCCESS(copyList(articleList.getRecords(),true,true));
    }

    /**
     * 这是文章分页的逻辑，这单表写下去没有问题，然后就是vo了
     * 关键在于判断是否需要多表
     * @param pagParam
     * @return
     */
//    @Override
//    public Result getArticleList(PagParam pagParam) {
//        Page<Article> articlePage = new Page<>(pagParam.getPage(),pagParam.getPageSize());
//        LambdaQueryWrapper<Article> query = new LambdaQueryWrapper();
//        if (pagParam.getCategoryId() != null) {
//            query.eq(Article::getCategoryId,pagParam.getCategoryId());
//        }
//        //如果有标签参数，则根据标签id获取指定文章，因为没有直接属性，则用in来解决
//        if (pagParam.getTagId()!=null){
//            List<Long> articleIds = new ArrayList<>();
//            LambdaQueryWrapper<ArticleTag> ArticleTagWrapper = new LambdaQueryWrapper<>();
//            ArticleTagWrapper.eq(ArticleTag::getTagId,pagParam.getTagId())
//                .select(ArticleTag::getArticleId);
//            List<ArticleTag> articleTags = articleTagMapper.selectList(ArticleTagWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIds.add(articleTag.getArticleId());
//            }
//            //in方法需要防止集合为空，所以做一次判断
//            if (articleIds.size() > 0) {
//                query.in(Article::getId,articleIds);
//            }
//        }
//        //根据year、month来获取对应的
//        if(pagParam.getMonth()!=null){
//
//
//        }
//        //order by weight,createDate
//        query.orderByDesc(Article::getWeight,Article::getCreateDate);
//
//        Page<Article> articlePage1 = articleMapper.selectPage(articlePage, query);
//        List<ArticleVo> articleVoList=copyList(articlePage1.getRecords(),true,true);
//        return new Result().SUCCESS(articleVoList);
//    }



    /**
     * 通过mapper获得最热文章
     * @param limit 获得条数
     * @return
     */
    @Override
    public Result  hotArticleList (int limit){
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper();
        /* select * from ms_article ORDER BY view_counts desc limit 5 */
        wrapper.orderByDesc(Article::getViewCounts)
                .last("limit "+limit)
                .select(Article::getId,Article::getTitle);
        List<Article> articles = articleMapper.selectList(wrapper);
        return Result.SUCCESS(copyList(articles,false,false));
    }

    @Override
    public Result newArticleList(int limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper();
        /*select * from ms_article ORDER BY create_date desc limit 5*/
        wrapper.orderByDesc(Article::getCreateDate)
                .last("limit "+limit)
                .select(Article::getId,Article::getTitle);
        List<Article> articles = articleMapper.selectList(wrapper);

        return Result.SUCCESS(copyList(articles,false,false));
    }

    @Override
    public Result ListArchives() {
        List<Archives> archives = articleMapper.selectListArchives();
        return Result.SUCCESS(archives);
    }

    /**
     * 文章详情，给加了isBody，iCategory支持，
     * @param articleId
     * @return
     */
    @Override
    public Result findArticleById(Long articleId) {
        //文章详情操作
        Article article = articleMapper.selectById(articleId);
        ArticleVo copy = copy(article, true, true,true,true);
        threadService.updateViewContents(articleMapper,article);
        return Result.SUCCESS(copy);
    }

    @Override
    public Result publishArticle(ArticleParam articleParam) {
//        按理来说是不是应该要一个参数验证
        Article article = new Article();
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        //commentCounts、viewCounts：评论数量怎么存储，先为默认值0吧
        article.setCommentCounts(0);
        article.setViewCounts(0);
        //记住要与存进去的拦截器为同一线程
        article.setAuthorId(UserThreadLocal.get().getId());
        //为什么这里不传一个id就够了而是categoryVo呢？
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        article.setWeight(0);
        article.setCreateDate(System.currentTimeMillis());

        articleMapper.insert(article);
//       注意还有标签是中间表要添加数据、articleBody
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        bodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());

//        需要判断tagList是否为空
        if (articleParam.getTags()!=null) {
            for (TagVo tag : articleParam.getTags()) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTagMapper.insert(articleTag);
            }
        }

        articleMapper.updateById(article);
//        这里返回一个对象就可以，那么返回map也可以
        Map<String, String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.SUCCESS(map);
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isAuthor,boolean isTag) {
        List<ArticleVo> list=new ArrayList<>();
        for (Article record : records) {
            list.add(copy(record,isAuthor,isTag,false,false));
        }
        return list;
    }
    private ArticleVo copy(Article record,boolean isAuthor,boolean isTag,boolean isBody,boolean isCategory) {

            ArticleVo articleVo = new ArticleVo();
            BeanUtils.copyProperties(record,articleVo);
            //这里因为Vo的Id是String，record的id是Integer，所以工具类没有copy到，需要手动设置一下
            articleVo.setId(record.getId().toString());

            //向vo传时间，因为最热文章不查询出时间，所以这里每次传的都是最热时间而不会报错，每个vo传的都是最新时间
            articleVo.setCreateDate(new DateTime(record.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
            if (isAuthor)
                //根据实体的作者id获得用户表的account名字传入
                //他这里的service用的是自己写的并且还判断了下，没有的话还加了默认值
                //这里注意因为是测试数据，作者id可能为null，这时就会报错
            {
                articleVo.setAuthor(userService.getById(record.getAuthorId()).getAccount());
            }
            if (isTag) {
                articleVo.setTags(tagService.selectTagsByArticleId(record.getId()));
            }
            if (isBody) {
                ArticleBody articleBody = bodyMapper.selectById(record.getBodyId());
                ArticleBodyVo articleBodyVo = new ArticleBodyVo();
                articleBodyVo.setContent(articleBody.getContent());
                articleVo.setBody(articleBodyVo);
            }
            //类别表与文章详情解藕
            if (isCategory) {
                Long categoryId = record.getCategoryId();
                articleVo.setCategory(categoryService.findCategoryById(categoryId));
            }

            return articleVo;

    }
}
