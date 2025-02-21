package com.LS.article.service.impl;

import com.LS.article.dao.ArticleHeadlineDao;
import com.LS.article.dao.UserDao;
import com.LS.article.dao.impl.ArticleHeadlineDaoImpl;
import com.LS.article.dao.impl.UserDaoImpl;
import com.LS.article.pojo.ArticleAttachment;
import com.LS.article.pojo.ArticleHeadline;
import com.LS.article.pojo.vo.HeadlineDetailVo;
import com.LS.article.pojo.vo.HeadlinePageVo;
import com.LS.article.pojo.vo.HeadlineQueryVo;
import com.LS.article.service.ArticleHeadlineService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleHeadlineServiceImpl implements ArticleHeadlineService {
    private ArticleHeadlineDao headlineDao = new ArticleHeadlineDaoImpl();

    /*
        totalPage:1,
        totalSize:1
        pageData:[
                    {
                       "hid":"1",                     // 文章id
                        "title":" ... ...",     // 文章标题
                        "type":"1",                    // 文章所属类别编号
                        "pageViews":"40",              // 文章浏览量
                        "pastHours":"3",              // 发布时间已过小时数
                        "publisher":"1"
                   }
                 ],
        pageNum:1,
        pageSize:1,
    */


    // 批量添加附件
    @Override
    public void uploadAttachments(List<ArticleAttachment> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            System.out.println("附件列表为空，跳过上传操作。");
            return;
        }
        System.out.println("开始上传附件，数量：" + attachments.size());
        headlineDao.addAttachments(attachments);
        System.out.println("附件上传完成。");
    }


    @Override
    public Map<String, Object> findPage(HeadlineQueryVo headlineQueryVo) {
        int pageNum = headlineQueryVo.getPageNum();
        int pageSize = headlineQueryVo.getPageSize();
        List<HeadlinePageVo> pageData = headlineDao.findPageList(headlineQueryVo);
        int totalSize = headlineDao.findPageCount(headlineQueryVo);

        int totalPage = totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("pageNum", pageNum);
        pageInfo.put("pageSize", pageSize);
        pageInfo.put("totalSize", totalSize);
        pageInfo.put("totalPage", totalPage);
        pageInfo.put("pageData", pageData);
        return pageInfo;
    }

    @Override
    public HeadlineDetailVo findHeadlineDetail(int hid) {

        // 修改该文章的浏览量 +1
        headlineDao.incrPageViews(hid);
        // 查询文章的详情
        return headlineDao.findHeadlineDetail(hid);
    }

    @Override
    public int addArticleHeadline(ArticleHeadline articleHeadline) {
        return headlineDao.addArticleHeadline(articleHeadline);
    }

    @Override
    public ArticleHeadline findByHid(Integer hid) {
        return headlineDao.findByHid(hid);
    }

    @Override
    public int update(ArticleHeadline articleHeadline) {
        return headlineDao.update(articleHeadline);
    }

    @Override
    public int removeByHid(int hid) {
        return headlineDao.removeByHid(hid);
    }

    @Override
    public List<ArticleHeadline> findArticlesByTypeId(int typeId) {
        return headlineDao.findArticlesByTypeId(typeId);
    }

    @Override
    public List<ArticleAttachment> getAttachmentsByHid(int hid) {
        return headlineDao.getAttachmentsByHid(hid);
    }
}

