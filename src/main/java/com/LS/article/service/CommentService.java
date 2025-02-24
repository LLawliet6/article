package com.LS.article.service;

import com.LS.article.common.Result;
import com.LS.article.dao.CommentDao;
import com.LS.article.pojo.Comment;

import java.util.List;

public interface CommentService {
    /**
     *保存评论
     */
    Result saveComment(int articleId, int userId, String content, Integer pid);//保存评论

    /**
     * 通过文章id查找评论
     */
    Result<List<Comment>> findCommentsByArticleId(int articleId);   // 根据文章ID查询评论
}


