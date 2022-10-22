package com.z2011.blogapi.dao.pojo;

import lombok.Data;

/**
 * 评论表
 * 可以看出这里用了很多Long，我们记得雪花算法生成的id为19位，时间戳在java是14位，数据库是10位，Integer最多是9位，所以很多都要Long
 */
@Data
public class Comment {
    private Long id;
    private String content;
    private Long createDate;
    private Long articleId;
    private Long authorId;
    private Long parentId;
    private Long toUid;
    private Integer level;
}
