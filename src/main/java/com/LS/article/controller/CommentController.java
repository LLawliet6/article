package com.LS.article.controller;

import com.LS.article.common.Result;
import com.LS.article.dao.CommentDao;
import com.LS.article.dao.impl.CommentDaoImpl;
import com.LS.article.pojo.Comment;
import com.LS.article.service.CommentService;
import com.LS.article.service.impl.CommentServiceImpl;
import com.LS.article.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.List;



//@WebServlet("/comment/*")
//public class CommentController extends BaseController {
//    private CommentService commentService = new CommentServiceImpl();
//
//    /**
//     * 保存评论
//     */
//    protected void saveComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        // 从请求中获取评论数据
//        int articleId = Integer.parseInt(req.getParameter("articleId"));
//        int userId = Integer.parseInt(req.getParameter("userId"));
//        String content = req.getParameter("content");
//
//        // 调用服务层保存评论
//        Result result = commentService.saveComment(articleId, userId, content);
//
//        // 返回 JSON 格式响应
//        WebUtil.writeJson(resp, result);
//    }
//
//    /**
//     * 根据文章ID查询评论列表
//     */
//    protected void findCommentsByArticleId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int articleId = Integer.parseInt(req.getParameter("articleId"));
//        Result<List<Comment>> result = commentService.findCommentsByArticleId(articleId);
//
//        WebUtil.writeJson(resp, result);
//    }
//}
@WebServlet("/comment/*")
public class CommentController extends BaseController {
    private CommentService commentService = new CommentServiceImpl();

    /**
     * 保存评论
     */
    protected void saveComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 从请求中获取评论数据
        int articleId = Integer.parseInt(req.getParameter("articleId"));
        int userId = Integer.parseInt(req.getParameter("userId"));
        String content = req.getParameter("content");
        String pidStr = req.getParameter("pid");

        // 顶级评论时 pid 为 null
        Integer pid = null;
        if (pidStr != null && !pidStr.isEmpty()) {
            pid = Integer.parseInt(pidStr);
        }

        // 调用服务层保存评论，传递 pid
        Result result = commentService.saveComment(articleId, userId, content, pid);

        // 返回 JSON 格式响应
        WebUtil.writeJson(resp, result);
    }

    /**
     * 根据文章ID查询评论列表
     */
    protected void findCommentsByArticleId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int articleId = Integer.parseInt(req.getParameter("articleId"));
        Result<List<Comment>> result = commentService.findCommentsByArticleId(articleId);

        WebUtil.writeJson(resp, result);
    }
}



