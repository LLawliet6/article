package com.LS.article.controller;

import com.LS.article.common.Result;
import com.LS.article.pojo.ArticleAttachment;
import com.LS.article.pojo.ArticleHeadline;
import com.LS.article.pojo.vo.HeadlinePageVo;
import com.LS.article.service.ArticleHeadlineService;
import com.LS.article.service.impl.ArticleHeadlineServiceImpl;
import com.LS.article.util.JwtHelper;
import com.LS.article.util.WebUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@MultipartConfig(
        location = "D:/javacode/LS_article/uploads", // 设置上传文件的临时目录
        fileSizeThreshold = 1024 * 1024 * 2*10, // 2MB
        maxFileSize = 1024 * 1024 * 50, // 50MB
        maxRequestSize = 1024 * 1024 * 100 // 100MB
)
@WebServlet("/headline/*")
public class ArticleHeadlineController extends BaseController {
    private ArticleHeadlineService headlineService = new ArticleHeadlineServiceImpl();

    /**
     * 收藏文章实现
     */
    protected void favorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer hid = Integer.parseInt(req.getParameter("hid"));
        String token = req.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        if (userId == null) {
            WebUtil.writeJson(resp, Result.build(33, 333, "未登录"));
            return;
        }
        // 判断当前用户是否已收藏
        boolean isFavorited = headlineService.isFavorited(userId.intValue(), hid);

        String resultMessage;
        if (isFavorited) {
            // 如果已经收藏，取消收藏
            headlineService.removeFavorite(userId.intValue(), hid);
            resultMessage = "未收藏";  // 返回“未收藏”，以便前端更新
        } else {
            // 如果没有收藏，进行收藏
            headlineService.addFavorite(userId.intValue(), hid);
            resultMessage = "已收藏";  // 返回“已收藏”，以便前端更新
        }

        // 返回结果，前端根据此信息更新按钮状态
        WebUtil.writeJson(resp, Result.ok(resultMessage));
    }

    protected void favoriteStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer hid = Integer.parseInt(req.getParameter("hid"));
        String token = req.getHeader("token");
        Long userId = JwtHelper.getUserId(token);

        if (userId == null) {
            WebUtil.writeJson(resp, Result.build(33, 333, "未登录"));
            return;
        }

        // 判断当前用户是否已收藏
        boolean isFavorited = headlineService.isFavorited(userId.intValue(), hid);

        // 返回当前收藏状态
        if (isFavorited) {
            WebUtil.writeJson(resp, Result.ok("已收藏"));
        } else {
            WebUtil.writeJson(resp, Result.ok("未收藏"));
        }
    }

    protected void myFavorites(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取当前登录用户
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);

        if (userId == null) {
            WebUtil.writeJson(response, Result.build(33, 333, "未登录"));
            return;
        }
        // 查询收藏文章列表
        List<HeadlinePageVo> favoriteList = headlineService.getMyFavorites(userId.intValue());

        // 返回结果
        WebUtil.writeJson(response, Result.ok(favoriteList));
    }

    protected void cancelFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int hid = Integer.parseInt(req.getParameter("hid"));
        headlineService.cancelFavorite(hid);
        WebUtil.writeJson(resp, Result.ok(null));
    }


    /**
     * 删除文章实现
     */
    protected void removeByHid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int hid = Integer.parseInt(req.getParameter("hid"));
        headlineService.removeByHid(hid);
        WebUtil.writeJson(resp, Result.ok(null));
    }

    /**
     * 更新文章实现
     */
//    protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ArticleHeadline articleHeadline = WebUtil.readJson(req, ArticleHeadline.class);
//        headlineService.update(articleHeadline);
//        WebUtil.writeJson(resp, Result.ok(null));
//    }
    protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> formFields = new HashMap<>();
        String imagePath = null;
        String uploadDir = req.getServletContext().getRealPath("/uploads");
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs(); // 创建目录
        }

        // 处理表单和文件上传
        for (Part part : req.getParts()) {
            String name = part.getName();
            if ("image".equals(name)) {
                String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                if (fileName != null && !fileName.isEmpty()) {
                    String uniqueFileName = UUID.randomUUID() + "_" + fileName;
                    imagePath = "uploads/" + uniqueFileName; // 相对路径
                    part.write(uploadDir + File.separator + uniqueFileName); // 保存图片到服务器
                }
            } else {
                // 保存其他表单字段
                try (InputStream inputStream = part.getInputStream();
                     Scanner scanner = new Scanner(inputStream, "UTF-8")) {
                    scanner.useDelimiter("\\A");
                    formFields.put(name, scanner.hasNext() ? scanner.next() : ""); // 读取表单值
                }
            }
        }

        // 获取文章 ID
        String hid = formFields.get("hid");
        if (hid == null) {
            WebUtil.writeJson(resp, Result.build(null, 668, "文章 ID 为空"));
            return;
        }

        // 查询原文章
        ArticleHeadline existingArticle = headlineService.findByHid(Integer.parseInt(hid));
        if (existingArticle == null) {
            WebUtil.writeJson(resp, Result.build(null, 669, "文章不存在"));
            return;
        }

        String existingImagePath = existingArticle.getImageUrl();

        // 如果有新图片上传，删除旧图片
        if (imagePath != null) {
            if (existingImagePath != null && !existingImagePath.isEmpty()) {
                File oldImageFile = new File(req.getServletContext().getRealPath(existingImagePath));
                if (oldImageFile.exists()) {
                    oldImageFile.delete();
                }
            }
        } else {
            // 如果没有新图片上传，保持原图片路径
            imagePath = existingImagePath;
        }

        // 构建更新对象
        ArticleHeadline articleHeadline = new ArticleHeadline();
        articleHeadline.setHid(Integer.parseInt(hid)); // 文章ID
        articleHeadline.setTitle(formFields.get("title"));
        articleHeadline.setType(Integer.parseInt(formFields.get("type")));
        articleHeadline.setArticle(formFields.get("article"));
        articleHeadline.setImageUrl(imagePath);

        // 调用服务层更新
        headlineService.update(articleHeadline);

        WebUtil.writeJson(resp, Result.ok("更新成功"));
    }


    /**
     * 修改文章回显
     */
    protected void findHeadlineByHid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer hid = Integer.parseInt(req.getParameter("hid"));
        ArticleHeadline headline = headlineService.findByHid(hid);

        Map<String, Object> data = new HashMap<>();
        data.put("headline", headline);
        WebUtil.writeJson(resp, Result.ok(data));
    }

    /**
     * 发布文章实现
     */
//    protected void publish(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String token = req.getHeader("token");
//        Long userId = JwtHelper.getUserId(token);
//
//        // 获取文件上传路径
//        String uploadDir = req.getServletContext().getRealPath("/uploads");
//        File uploadDirFile = new File(uploadDir);
//        if (!uploadDirFile.exists()) {
//            uploadDirFile.mkdirs(); // 创建目录
//        }
//
//        // 检查请求是否是 multipart/form-data
//        if (req.getContentType() == null || !req.getContentType().startsWith("multipart/form-data")) {
//            WebUtil.writeJson(resp, Result.build(null, 666, "请求格式不正确"));
//            return;
//        }
//
//        // 使用 IO 流处理文件和其他表单数据
//        Map<String, String> formFields = new HashMap<>();
//        String imagePath = null;
//
//        for (Part part : req.getParts()) {
//            String name = part.getName();
//            if ("image".equals(name)) {
//                // 保存图片文件
//                String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
//                String uniqueFileName = UUID.randomUUID() + "_" + fileName;
//                imagePath = "uploads/" + uniqueFileName; // 相对路径
//                part.write(uploadDir + File.separator + uniqueFileName); // 写入绝对路径
//            } else {
//                // 保存其他表单字段
//                try (InputStream inputStream = part.getInputStream();
//                     Scanner scanner = new Scanner(inputStream, "UTF-8")) {
//                    scanner.useDelimiter("\\A");
//                    formFields.put(name, scanner.hasNext() ? scanner.next() : ""); // 读取表单值
//                }
//            }
//        }
//
//        // 构建 ArticleHeadline 对象
//        ArticleHeadline articleHeadline = new ArticleHeadline();
//        articleHeadline.setTitle(formFields.get("title"));
//        articleHeadline.setType(Integer.parseInt(formFields.get("type")));
//        articleHeadline.setArticle(formFields.get("article"));
//        articleHeadline.setPublisher(userId.intValue());
//        articleHeadline.setImageUrl(imagePath); // 设置图片路径
//
//        // 调用服务层存储到数据库
//        headlineService.addArticleHeadline(articleHeadline);
//
//        WebUtil.writeJson(resp, Result.ok(null));
//    }
    protected void publish(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        if (userId == null) {
            WebUtil.writeJson(resp, Result.build(null, 401, "未检测到有效用户，请先登录"));
            return;
        }

        // 文件上传路径（用于存储文章封面图片）
        String uploadDir = req.getServletContext().getRealPath("/uploads");
        System.out.println("文件上传目录：" + uploadDir);
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
            System.out.println("创建了文件上传目录");
        }

        if (req.getContentType() == null || !req.getContentType().startsWith("multipart/form-data")) {
            WebUtil.writeJson(resp, Result.build(null, 666, "请求格式不正确"));
            return;
        }

        // 解析表单字段和图片文件（附件在前端已单独上传，不在此处理）
        Map<String, String> formFields = new HashMap<>();
        String imagePath = null;

        for (Part part : req.getParts()) {
            String name = part.getName();
            String fileName = part.getSubmittedFileName();

            // 如果是文件且字段名称为 image，则处理图片上传
            if (fileName != null && !fileName.isEmpty() && "image".equals(name)) {
                String uniqueFileName = UUID.randomUUID() + "_" + fileName;
                String filePath = "uploads/" + uniqueFileName;
                part.write(uploadDir + File.separator + uniqueFileName);
                imagePath = filePath;
            } else if (fileName == null || fileName.isEmpty()) { // 非文件字段
                try (InputStream inputStream = part.getInputStream();
                     Scanner scanner = new Scanner(inputStream, "UTF-8")) {
                    scanner.useDelimiter("\\A");
                    formFields.put(name, scanner.hasNext() ? scanner.next() : "");
                }
            }
        }

        System.out.println("接收到的表单字段：" + formFields);
        System.out.println("接收到的图片路径：" + imagePath);

        // 构建文章对象
        ArticleHeadline articleHeadline = new ArticleHeadline();
        articleHeadline.setTitle(formFields.get("title"));
        articleHeadline.setType(Integer.parseInt(formFields.get("type")));
        articleHeadline.setArticle(formFields.get("article"));
        articleHeadline.setPublisher(userId.intValue());
        articleHeadline.setImageUrl(imagePath);

        // 插入文章，获取生成的文章ID（hid）
        int hid = headlineService.addArticleHeadline(articleHeadline);
        System.out.println("文章插入成功，生成的文章 ID：" + hid);
        // 2. 获取附件ID数组（前端传的是 JSON 数组）
        String attachmentIdsJson = req.getParameter("attachmentIds");

        List<Integer> attachmentIds = new ArrayList<>();
        if (attachmentIdsJson != null && !attachmentIdsJson.isEmpty()) {
            attachmentIds = new Gson().fromJson(attachmentIdsJson, new TypeToken<List<Integer>>(){}.getType());
        }
        // 4. 关联附件ID到文章
        if (!attachmentIds.isEmpty()) {
            headlineService.updateAttachmentsHid(hid, attachmentIds);
            System.out.println("附件已关联到文章：" + attachmentIds);
        }

        WebUtil.writeJson(resp, Result.ok(null));
    }




    protected void attachmentUploads(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uploadDir = req.getServletContext().getRealPath("/uploads");
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) uploadDirFile.mkdirs();

        // 关键修改点：只处理单个文件
        Part filePart = req.getPart("attachment");
        // 关键修复：检查是否为 null
        if (filePart == null) {
            WebUtil.writeJson(resp, Result.build(null, 500, "未接收到文件"));
            return;
        }
        String fileName = filePart.getSubmittedFileName();

        if (fileName != null && !fileName.isEmpty()) {
            // 生成唯一文件名
            String uniqueFileName = UUID.randomUUID() + "_" + fileName;
            String filePath = "uploads/" + uniqueFileName;

            // 保存文件
            filePart.write(uploadDir + File.separator + uniqueFileName);

            // 创建附件对象
            ArticleAttachment attachment = new ArticleAttachment();
            attachment.setHid(null); // 临时值
            attachment.setFileName(fileName);
            attachment.setFileUrl(filePath);

            // 插入数据库并获取生成的ID
            int attachmentId = headlineService.uploadAttachment(attachment);

            WebUtil.writeJson(resp, Result.ok(attachmentId));
        } else {
            WebUtil.writeJson(resp, Result.build(null, 666, "文件名为空"));
        }
    }


}

