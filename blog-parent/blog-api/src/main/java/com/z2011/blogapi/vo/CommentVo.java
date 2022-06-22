package com.z2011.blogapi.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo {
//      Comment实体类
//    private Long id;
//    private String content;
//    private Long create_date;时间戳要转换成字符串
//    private Long articleId;vo里面没用了，舍弃
//    private Long authorId;要转换成SysUserVo
//    private Long parentId;数据库中指向上层评论，通过这个实现childrens放什么
//    private Long toUid;
//    private Integer level;

// @JsonSerialize(using= ToStringSerializer.class)
    private String id;
    private String content;
    //需要进行转换
    private String createDate;
    private SysUserVo author;
    private List<CommentVo> childrens;
    private Integer level;
    private SysUserVo toUser;

}
