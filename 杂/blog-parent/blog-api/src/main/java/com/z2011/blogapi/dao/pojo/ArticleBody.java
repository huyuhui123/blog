package com.z2011.blogapi.dao.pojo;

import lombok.Data;

/**
 * @author z2011
 */
@Data
public class ArticleBody {
    private Long id;
    private String content;
    private String contentHtml;
    private Long articleId;
}
