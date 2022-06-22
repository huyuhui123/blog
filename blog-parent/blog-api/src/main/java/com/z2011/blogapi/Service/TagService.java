package com.z2011.blogapi.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.pojo.Tag;
import com.z2011.blogapi.vo.TagVo;

import java.util.List;

public interface TagService extends IService<Tag> {

    List<TagVo> selectTagsByArticleId(Long articleId);

    /**
     * 查找最热标签tagVo（id，name），需要通过ms_article_tag表排序，article_id应该是有重复的，所以
     * 判读这些数据按照一个字段分组会有多少种（count）然后排序
     *
     * @param limit
     * @return
     */
    Result hots(int limit);

    /**用在提交文章的地方
     * @return
     */
    Result selectAllTags();

    /**
     * 获得所有标签所有字段
     * @return
     */
    Result selectAllTagDetails();

    Result selectTagDetailById(Long id);
}
