package com.z2011.blogapi.Service;

import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.vo.params.CommentParam;

public interface CommentService {
    /**
     * 通过文章Id获得List<CommentVo>，查询
     * children是通过level
     * @param id
     * @return
     */
    Result findCommentsByArticleId(String id);

    /**
     * 提交评论
     * 首先需要判断登录，然后根据获取的用户信息与传入的参数保存到数据库里
     * @return
     * @param commentParam
     */
    Result comment(CommentParam commentParam);
}
