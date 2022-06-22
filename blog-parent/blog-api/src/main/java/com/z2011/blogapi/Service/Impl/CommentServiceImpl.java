package com.z2011.blogapi.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.z2011.blogapi.Service.CommentService;
import com.z2011.blogapi.Service.SysUserService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.mapper.CommentMapper;
import com.z2011.blogapi.dao.pojo.Comment;
import com.z2011.blogapi.dao.pojo.SysUser;
import com.z2011.blogapi.utils.UserThreadLocal;
import com.z2011.blogapi.vo.CommentVo;
import com.z2011.blogapi.vo.params.CommentParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService userService;


    /**
     * 根据文章id获取评论，根据作者id获取作者信息，childrens
     * 也就是说是个二层list
     * 这个地方可不可以写xml？就三步，不需要，而且麻烦
     * @param id
     * @return
     */
    @Override
    public Result findCommentsByArticleId(String id) {
        //根据文章id获得评论list
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper();
        commentWrapper.eq(Comment::getArticleId,id);
        commentWrapper.eq(Comment::getLevel,1);
        commentWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentMapper.selectList(commentWrapper);

        List<CommentVo> commentVos=copyList(comments);
        return Result.SUCCESS(commentVos);
    }

    /**
     * 创建评论
     * 根据登录拦截器判断是否登录并通过UserThreadLocal获得登录用户信息，参数验证，进行存入评论
     * 前端对文章进行评论时CommentParam是不会有parent、toUserId两个参数，
     * toUserId设置的和parent一个是对设计结构的兼容，这个结构可以支持多层评论，但这里先只写两层
     * @param commentParam
     * @return
     */
    @Override
    public Result comment(CommentParam commentParam) {
        //insert into comment(content参数获得,create_date系统时间,article_id参数获得,author_id ThreadLocal获得，parent_id参数获得，to_uid参数获得，level) values()
        SysUser sysUser = UserThreadLocal.get();
        //commentMapper.insert();的传入值，不设置id
        Comment comment = new Comment();
        comment.setContent(commentParam.getContent());
        //存储当前时间时间戳
        comment.setCreateDate(System.currentTimeMillis());
        comment.setArticleId(commentParam.getArticleId());
        //在设置里面最难的就是层级设置的判断
        Long parentId = commentParam.getParent();
        Long toUserId = commentParam.getToUserId();
        if (parentId== null || parentId.equals(0L) || toUserId== null || toUserId.equals(0L)){
            comment.setLevel(1);
            //数据库里设了非空，所以需要值，那为什么不在数据库里设置好？
            comment.setParentId(0L);
            comment.setToUid(0L);
        }else {
            comment.setLevel(2);
            comment.setToUid(commentParam.getToUserId());
            comment.setParentId(commentParam.getParent());
        }
        comment.setAuthorId(sysUser.getId());
        commentMapper.insert(comment);
        return Result.SUCCESS(null);
    }

    /**
     * 把查询出来的List<Comment>转换成List<CommentVo>
     *   arraylist特点：  数组集合，可变数组，线程不安全
     * @param comments
     * @return
     */
    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVos=new ArrayList();
        for (Comment comment : comments) {
                CommentVo commentVo=copy(comment);
                commentVos.add(commentVo);


        }
        return commentVos;
    }

    /**
     * 把Comment转换成CommentVo
     * 那第一步肯定是BeanUtils.copyProperties(comment,commentVo)，转换了id、content、level
     * children是通过level为1则往里面填，不是则不填
     * 这里就不像article判断是否需要body、标签、分类等了
     * createDate需要转换时间戳
     * SysUserVo author作者信息需要UserService
     * childrens：是一个list，甚至还是个CommentVoList，它，需要对评论表再查一遍，直接用mapper，输入单个评论id来查询其他评论的parentId属性从而填充到自己的childrens里
     * 因为是二层，所以不用无线递归，
     * 我通过ParentId得到CommentList，这还要转一次vo，这里先写死，以后再搞递归
     *     private Long id;
     *     private String content;
     *     private String createDate;
     *     private SysUserVo author;
     *     private List<CommentVo> childrens;
     *     private Integer level;
     *     private SysUserVo toUser;
     *     这里面最难的就是childrens，comment转commentVo
     *    重要： level为1时需要childrens赋值，不需要touser赋值
     *
     * @param comment
     * @return
     */
    private CommentVo copy(Comment comment) {
        //copy是一个对返回Vo中一个单数的操作
        CommentVo commentVo = new CommentVo();
        //可以赋值id、content、level
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(comment.getId().toString());
        //private String createDate;
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //private SysUserVo author;select
        commentVo.setAuthor(userService.findUserVoById(comment.getAuthorId()));
        //childrens：level为1则为一级评论，大于一则为底层评论，需要在全表找子评论list，然后再调用copyList到本身
        Integer level = comment.getLevel();
        if (level==1){
//            select * from comment where parentId={comment.id}
            LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
            commentWrapper.eq(Comment::getParentId,comment.getId());
            List<Comment> comments = commentMapper.selectList(commentWrapper);
            List<CommentVo> commentVos = copyList(comments); //转commentVo
            commentVo.setChildrens(commentVos);
        }
        if (level>1){
            commentVo.setToUser(userService.findUserVoById(comment.getToUid()));
        }
        return commentVo;
    }

}
