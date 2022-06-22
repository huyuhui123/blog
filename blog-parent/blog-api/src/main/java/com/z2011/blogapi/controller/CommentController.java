package com.z2011.blogapi.controller;

import com.z2011.blogapi.Service.CommentService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 文章详情的评论
     * @param id ：文章id
     * @return
     */
    @GetMapping("article/{id}")
    public Result findCommentsByArticleId(@PathVariable String id){
       return commentService.findCommentsByArticleId(id);
    }
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.comment(commentParam);
    }
}
