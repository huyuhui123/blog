package com.z2011.blogapi.controller;

import com.z2011.blogapi.Service.TagService;
import com.z2011.blogapi.dao.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagsController {
    @Autowired
    private TagService tagService;
    /*返回最热标签*/
    @GetMapping("hot")
    public Result getHotTag(){
        int limit=6;
        return  tagService.hots(limit);
    }
    @GetMapping
    public Result getAllTags(){
        return tagService.selectAllTags();
    }
    @GetMapping("detail")
    public Result getAllTagDetails(){
        return tagService.selectAllTagDetails();
    }
//    categorys/detail/{id}
    @GetMapping("detail/{id}")
    public Result getTagDetailById(@PathVariable Long id){
        return tagService.selectTagDetailById(id);
    }
}
