package com.LS.article.controller;

import com.LS.article.common.Result;
import com.LS.article.pojo.ArticleAttachment;
import com.LS.article.pojo.ArticleHeadline;
import com.LS.article.pojo.ArticleType;
import com.LS.article.pojo.vo.HeadlineDetailVo;
import com.LS.article.pojo.vo.HeadlineQueryVo;
import com.LS.article.service.ArticleHeadlineService;
import com.LS.article.service.ArticleTypeService;
import com.LS.article.service.impl.ArticleHeadlineServiceImpl;
import com.LS.article.service.impl.ArticleTypeServiceImpl;
import com.LS.article.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/portal/*")
public class PortalController extends BaseController {
    private ArticleTypeService typeService = new ArticleTypeServiceImpl();
    private ArticleHeadlineService headlineService = new ArticleHeadlineServiceImpl();

    // 添加分类
    protected void addType(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tname = req.getParameter("tname");
        ArticleType articleType = new ArticleType();
        articleType.setTname(tname);
        int result = typeService.addType(articleType);
        if(result > 0) {
            WebUtil.writeJson(resp, Result.ok("新增成功"));
        } else {
            WebUtil.writeJson(resp, Result.build(null, 400,"增加失败"));
        }
    }

    // 更新分类
    protected void updateType(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int tid = Integer.valueOf(req.getParameter("tid"));
        String tname = req.getParameter("tname");
        ArticleType articleType = new ArticleType();
        articleType.setTid(tid);
        articleType.setTname(tname);
        int result = typeService.updateType(articleType);
        if(result > 0) {
            WebUtil.writeJson(resp, Result.ok("更新成功"));
        } else {
            WebUtil.writeJson(resp, Result.build(null, 400,"更新失败"));
        }
    }

    // 删除分类
    protected void deleteType(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int tid = Integer.valueOf(req.getParameter("tid"));
        int result = typeService.deleteType(tid);
        if(result > 0) {
            WebUtil.writeJson(resp, Result.ok("删除成功"));
        } else {
            WebUtil.writeJson(resp, Result.build(null, 400,"删除失败"));
        }
    }

    /**
     * 获取附件
     */
    protected void getAttachments(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hidParam = req.getParameter("hid");
        if (hidParam == null) {
            WebUtil.writeJson(resp, Result.build(null, 400, "缺少文章ID"));
            return;
        }

        try {
            int hid = Integer.parseInt(hidParam);
            List<ArticleAttachment> attachments = headlineService.getAttachmentsByHid(hid);
            WebUtil.writeJson(resp, Result.ok(attachments));
        } catch (NumberFormatException e) {
            WebUtil.writeJson(resp, Result.build(null, 400, "文章ID格式错误"));
        }
    }
    /**
     * 删除附件
     */
    protected void deleteAttachment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String aidParam = req.getParameter("aid");
        if (aidParam == null) {
            WebUtil.writeJson(resp, Result.build(null, 400, "缺少附件ID"));
            return;
        }

        try {
            int aid = Integer.parseInt(aidParam);
            // 调用服务层删除附件
            boolean result = headlineService.deleteAttachmentById(aid);

            if (result) {
                WebUtil.writeJson(resp, Result.ok("附件删除成功"));
            } else {
                WebUtil.writeJson(resp, Result.build(null, 400, "附件删除失败"));
            }
        } catch (NumberFormatException e) {
            WebUtil.writeJson(resp, Result.build(null, 400, "附件ID格式错误"));
        }
    }

    /**
     * 查询文章详情
     */
    protected void showHeadlineDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 接收要查询文章的hid
        int hid = Integer.parseInt(req.getParameter("hid"));
        // 调用服务层完成查询处理
        HeadlineDetailVo headlineDetailVo = headlineService.findHeadlineDetail(hid);
        // 将查到的信息响应给客户端
        Map<String, Object> data = new HashMap<>();
        data.put("headline", headlineDetailVo);
        WebUtil.writeJson(resp, Result.ok(data));
    }

    /**
     * 分页查询文章信息的实现
     */
    protected void findArticlePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 接收请求中的参数
        HeadlineQueryVo headlineQueryVo = WebUtil.readJson(req, HeadlineQueryVo.class);
        // 将参数传递给服务层 进行分页查询
        /*
         * pageData:[
         *   {
         *           "hid":"1",                     // 文章id
         *           "title":"... ... ...",   // 文章标题
         *           "type":"1",                    // 文章所属类别编号
         *           "pageViews":"40",              // 文章浏览量
         *           "pastHours":"3",              // 发布时间已过小时数
         *           "publisher":"1"             //用户id
         *           "publisherName": "liushuai",  // 发布者昵称
                    "typeName": "励志"              // 类别名称
         *   }
         *
         * ],
         * pageNum:1,
         * pageSize:1,
         * totalPage:1,
         * totalSize:1
         */
        // 检查type是否为null，如果为null则设置默认值
        if (headlineQueryVo.getType() == null) {
            headlineQueryVo.setType(0);
        }

        // 将参数传递给服务层进行分页查询
        Map<String, Object> pageInfo = headlineService.findPage(headlineQueryVo);
        Map<String, Object> data = new HashMap<>();
        data.put("pageInfo", pageInfo);

        // 将分页查询的结果转换成json响应给客户端
        WebUtil.writeJson(resp, Result.ok(data));
    }

    /**
     * 查询所有文章类型实现
     */
//    protected void findAllTypes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        // 查询所有的文章类型,装入Result响应给客户端
//        List<ArticleType> articleTypeList = typeService.findAll();
//        WebUtil.writeJson(resp, Result.ok(articleTypeList));
//    }
    protected void findAllTypes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 查询所有的文章类型
        List<ArticleType> articleTypeList = typeService.findAll();

        // 为每个类别查询对应的文章标题
        List<Map<String, Object>> result = new ArrayList<>();
        for (ArticleType type : articleTypeList) {
            Map<String, Object> typeWithArticles = new HashMap<>();
            typeWithArticles.put("tid", type.getTid());
            typeWithArticles.put("tname", type.getTname());

            // 根据类别ID查询该类别下的文章标题
            List<ArticleHeadline> articles = headlineService.findArticlesByTypeId(type.getTid());
            List<Map<String, Object>> articleTitles = new ArrayList<>();
            for (ArticleHeadline article : articles) {
                Map<String, Object> articleData = new HashMap<>();
                articleData.put("title", article.getTitle());
                articleData.put("hid", article.getHid());
                articleData.put("isDeleted", article.getIsDeleted());
                articleTitles.add(articleData);
            }
            // 将文章列表加入到类别数据中
            typeWithArticles.put("articles", articleTitles);
            result.add(typeWithArticles);
        }
        // 将类别及其对应的文章标题数据响应给客户端
        WebUtil.writeJson(resp, Result.ok(result));
    }

}
