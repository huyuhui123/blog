package com.z2011.blogapi.vo.params;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CommentParam {
    //评论时当前文章的id
    private Long articleId;
    //评论内容
    private String content;
    //对哪个评论进行评论
    private Long parent;
    //对哪个用户进行评论，在这里一般对哪个评论进行评论时，toUserId就是被评论的用户id
    private Long toUserId;
}
