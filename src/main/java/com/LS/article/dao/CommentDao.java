package com.LS.article.dao;

import com.LS.article.pojo.Comment;

import java.util.List;

public interface CommentDao {
    // 添加评论
    int saveComment(int articleId, int userId, String content, Integer pid);

     //根据文章ID查询评论列表
    List<Comment> findCommentsByArticleId(int articleId);

}

