package com.LS.article.service.impl;

import com.LS.article.common.Result;
import com.LS.article.dao.CommentDao;
import com.LS.article.dao.impl.CommentDaoImpl;
import com.LS.article.pojo.Comment;
import com.LS.article.service.CommentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentServiceImpl implements CommentService {
    private CommentDao commentDao = new CommentDaoImpl();

    @Override
    public Result saveComment(int articleId, int userId, String content, Integer pid) {
        // 传递 pid 给 DAO 层
        int rowsAffected = commentDao.saveComment(articleId, userId, content, pid);
        return rowsAffected > 0 ? Result.ok(null) : Result.build(null, 500, "评论保存失败");
    }

    @Override
    public Result<List<Comment>> findCommentsByArticleId(int articleId) {
        List<Comment> comments = commentDao.findCommentsByArticleId(articleId);
     // 构建父子评论树
        List<Comment> commentTree = buildCommentTree(comments);
        return Result.ok(commentTree);
    }

    @Override
    public Result deleteComment(int cid) {
        int rowsAffected=commentDao.deletById(cid);
        return rowsAffected > 0 ? Result.ok(null) : Result.build(null, 500, "评论删除失败");
    }

    /**
     * 构建评论树的方法，将评论按照父子关系进行嵌套
     */
    private List<Comment> buildCommentTree(List<Comment> comments) {
        List<Comment> rootComments = new ArrayList<>();
        Map<Integer, Comment> commentMap = new HashMap<>();

        // 先将所有评论放入 map 中，便于根据 cid 找到评论
        for (Comment comment : comments) {
            comment.setChildren(new ArrayList<>()); // 初始化子评论列表
            commentMap.put(comment.getCid(), comment);
        }

        // 遍历所有评论，建立父子关系
        for (Comment comment : comments) {
            if (comment.getPid() == null) {
                // 顶级评论，pid 为 null
                rootComments.add(comment);
            } else {
                // 子评论，找到对应的父评论并添加到父评论的子评论列表中
                Comment parentComment = commentMap.get(comment.getPid());
                if (parentComment != null) {
                    parentComment.getChildren().add(comment);
                }
            }
        }

        return rootComments;
    }
}

