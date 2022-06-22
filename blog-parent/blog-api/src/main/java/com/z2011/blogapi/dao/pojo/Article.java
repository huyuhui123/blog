package com.z2011.blogapi.dao.pojo;

import lombok.Data;

/**
 * 与Vo的差距主要在于四个属性：
 * Long authorId->String author 这个属性会从SysUser表里找（在这个数据库里关于用户表有admin表和SysUser表两个表）
 * Long bodyId->ArticleBodyVo body 这个同样也是涉及到别的表，逻辑在在copy的isBody判断里写了
 * List<TagVo> tags 这个article属性没有，是通过中间表获取的
 * Long categoryId->CategoryVo category 一样
 *
 */
@Data
public class Article {

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Long id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     *类别id
     */
    private Long categoryId;

    /**
     * 置顶
     */
    private Integer weight ;


    /**
     * 创建时间
     */
    private Long createDate;

}
