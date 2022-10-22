package com.z2011.blogapi.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.z2011.blogapi.dao.pojo.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章Id查找标签
     * @param articleId
     * @return
     */
    List<Tag> selectTagsByArticleId(Long articleId);

    /**
     * 查询最热的标签idList并返回
     * @param limit
     * @return
     */
    List<Long> findHotsTagIds(int limit);

    /**
     * 根据tagIdList获得tag实体
     * @param hotTagIds
     * @return
     */
    List<Tag> findTagsByTagsIds(List<Long> hotTagIds);
}
