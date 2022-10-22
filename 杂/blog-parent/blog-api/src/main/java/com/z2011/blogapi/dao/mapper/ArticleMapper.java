package com.z2011.blogapi.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.z2011.blogapi.dao.dos.Archives;
import com.z2011.blogapi.dao.pojo.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {

    List<Archives> selectListArchives();

    /**
     * plus转sql，自定义分页（参数和返回类型）估计是返回类型决定的
     * @param page
     * @param categoryId
     * @param tagId
     * @param year
     * @param month
     * @return
     */
    IPage<Article> articleList(IPage page,@Param("categoryId") Long categoryId,@Param("tagId") Long tagId,@Param("year") String year, @Param("month")String month);
}
