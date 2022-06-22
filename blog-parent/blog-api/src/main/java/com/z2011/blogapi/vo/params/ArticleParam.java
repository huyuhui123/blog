package com.z2011.blogapi.vo.params;

import com.z2011.blogapi.vo.CategoryVo;
import com.z2011.blogapi.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {
    private Long id;
    private String title;
    private ArticleBodyParam body;
    private CategoryVo category;
    private String summary;
    private List<TagVo> tags;

}
