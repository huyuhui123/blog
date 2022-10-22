package com.z2011.blogapi.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.z2011.blogapi.Service.TagService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.mapper.TagMapper;
import com.z2011.blogapi.dao.pojo.Tag;
import com.z2011.blogapi.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>  implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> selectTagsByArticleId(Long articleId){
        List<Tag> tags = tagMapper.selectTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public Result hots(int limit) {
        //查到对应tagId数组，根据数组找name用到了动态sql
        List<Long> hotTagIds=tagMapper.findHotsTagIds(limit);
        List<Tag> tags = tagMapper.findTagsByTagsIds(hotTagIds);
        return Result.SUCCESS(tags);
    }

    @Override
    public Result selectAllTags() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = tagMapper.selectList(wrapper);
        List<TagVo> tagVos = copyList(tags);
        return Result.SUCCESS(tagVos);
    }

    @Override
    public Result selectAllTagDetails() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = tagMapper.selectList(wrapper);
        List<TagVo> tagVos = copyList(tags);
        return Result.SUCCESS(tagVos);

    }

    @Override
    public Result selectTagDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.SUCCESS(copy(tag));
    }

    public List<TagVo> copyList(List<Tag> tags){
        ArrayList<TagVo> tagVos = new ArrayList<>();
        for (Tag tag : tags) {
            tagVos.add(copy(tag));
        }
        return tagVos;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setId(tag.getId().toString()); //
        return tagVo;
    }
}
