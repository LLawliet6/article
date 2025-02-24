package com.LS.article.dao.impl;

import com.LS.article.dao.BaseDao;
import com.LS.article.dao.CommentDao;
import com.LS.article.pojo.Comment;

import java.util.List;

public class CommentDaoImpl extends BaseDao implements CommentDao {
//    @Override
//    public int saveComment(int articleId, int userId, String content) {
//        String sql = "INSERT INTO comment (article_id, user_id, content) VALUES (?, ?, ?)";
//        return baseUpdate(sql, articleId, userId, content);
//    }

    public int saveComment(int articleId, int userId, String content, Integer pid) {
        String sql;
        if (pid != null) {
            sql = "INSERT INTO comment (article_id, user_id, content, pid) VALUES (?, ?, ?, ?)"; // 包含 pid
            return baseUpdate(sql, articleId, userId, content, pid);
        } else {
            sql = "INSERT INTO comment (article_id, user_id, content) VALUES (?, ?, ?)"; // 不包含 pid
            return baseUpdate(sql, articleId, userId, content);
        }
    }

//    @Override
//    public List<Comment> findCommentsByArticleId(int articleId) {
//        String sql = "SELECT c.cid, " +
//                "       c.article_id AS articleId, " +
//                "       c.user_id AS userId, " +
//                "       c.content, " +
//                "       c.create_time AS createTime, " +
//                "       u.username " +
//                "FROM comment c " +
//                "JOIN article_user u ON c.user_id = u.uid " +
//                "WHERE c.article_id = ? " +
//                "ORDER BY c.create_time DESC";
//
//        return baseQuery(Comment.class, sql, articleId);
//    }
public List<Comment> findCommentsByArticleId(int articleId) {
    String sql = "SELECT c.cid, " +
            "       c.article_id AS articleId, " +
            "       c.user_id AS userId, " +
            "       c.content, " +
            "       c.create_time AS createTime, " +
            "       u.username, " +
            "       c.pid, " +
            "       COALESCE(ru.username, '') AS replyUsername " + // 如果 ru.username 为 null，返回空字符串
            "FROM comment c " +
            "JOIN article_user u ON c.user_id = u.uid " +
            "LEFT JOIN comment rc ON c.pid = rc.cid " +
            "LEFT JOIN article_user ru ON rc.user_id = ru.uid " +
            "WHERE c.article_id = ? " +
            "ORDER BY c.create_time DESC";


    return baseQuery(Comment.class, sql, articleId);
}




//    public List<Comment> findChildComments(int parentId) {
//        String sql = "SELECT c.cid, c.article_id AS articleId, c.user_id AS userId, c.content, c.create_time AS createTime, u.username, c.pid " +
//                "FROM comment c " +
//                "JOIN article_user u ON c.user_id = u.uid " +
//                "WHERE c.pid = ? " +
//                "ORDER BY c.create_time ASC";
//        return baseQuery(Comment.class, sql, parentId);
//    }

}
