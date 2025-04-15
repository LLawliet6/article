package com.LS.article.service.impl;

import com.LS.article.dao.ArticleHeadlineDao;
import com.LS.article.dao.BaseDao;
import com.LS.article.dao.impl.ArticleHeadlineDaoImpl;
import com.LS.article.pojo.ArticleAttachment;
import com.LS.article.pojo.ArticleHeadline;
import com.LS.article.pojo.vo.AttachmentVo;
import com.LS.article.pojo.vo.HeadlineDetailVo;
import com.LS.article.pojo.vo.HeadlinePageVo;
import com.LS.article.pojo.vo.HeadlineQueryVo;
import com.LS.article.service.ArticleHeadlineService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArticleHeadlineServiceImpl extends BaseDao implements ArticleHeadlineService {
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


    @Override
    public boolean isFavorited(Integer userId, Integer hid) {
        return headlineDao.isFavorited(userId, hid);
    }

    @Override
    public void addFavorite(Integer userId, Integer hid) {
        headlineDao.addFavorite(userId, hid);
    }

    @Override
    public void removeFavorite(Integer userId, Integer hid) {
        headlineDao.removeFavorite(userId, hid);
    }

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

    //获取我的收藏
    @Override
    public List<HeadlinePageVo> getMyFavorites(Integer userId) {
        return headlineDao.getMyFavorites(userId);
    }

    @Override
    public int cancelFavorite(int hid) {
        return headlineDao.cancelFavorite(hid);
    }

    @Override
    public void updateAttachmentsHid(int hid, List<Integer> attachmentIds) {
        if (attachmentIds == null || attachmentIds.isEmpty()) {
            return;
        }

        String sql = "UPDATE article_attachment SET hid = ? WHERE aid IN (" +
                attachmentIds.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";
        List<Object> params = new ArrayList<>();
        params.add(hid);
        params.addAll(attachmentIds);

        baseUpdate(sql, params.toArray());
    }

    public int uploadAttachment(ArticleAttachment attachment) {
        return headlineDao.addAttachment(attachment);
    }

    /**
     * 删除附件
     *
     * @param aid 附件ID
     * @return 删除结果
     */
    public boolean deleteAttachmentById(int aid) {
        // 获取附件的文件路径
        String filePath = getFilePathByAid(aid);
        // 如果文件路径为空，说明文件不存在
        if (filePath == null) {
            System.out.println("文件路径无效，删除失败。");
            return false;
        }
        // 删除数据库中的记录
        String sqlDelete = "DELETE FROM article_attachment WHERE aid = ?";
        int rowsAffected = baseUpdate(sqlDelete, aid);
        if (rowsAffected > 0) {
            // 文件删除操作
            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("文件删除成功：" + filePath);
                    return true;
                } else {
                    System.out.println("文件删除失败：" + filePath);
                }
            } else {
                System.out.println("文件不存在，删除失败：" + filePath);
            }
        } else {
            System.out.println("数据库删除失败，附件ID：" + aid);
        }
        return false;
    }

   // 获取我的附件列表
   public List<AttachmentVo> getMyAttachments(Integer userId) {
       return headlineDao.getMyAttachments(userId);
   }


    /**
     * 根据附件id获取文件路径
     *
     * @param aid
     * @return
     */
    private String getFilePathByAid(int aid) {
        String sql = "SELECT aid AS aid, hid AS hid, file_name AS fileName, file_url AS fileUrl, upload_time AS uploadTime FROM article_attachment where aid = ?";
        List<ArticleAttachment> attachmentList = baseQuery(ArticleAttachment.class, sql, aid);

        // 检查附件列表是否为空，并取出第一个附件
        if (attachmentList != null && !attachmentList.isEmpty()) {
            ArticleAttachment attachment = attachmentList.get(0);  // 获取第一个附件

            // 假设 file_url 存储的是相对路径，构建文件的绝对路径
            String basePath = "D:/javadevelop/javacode/LS_article/target/LS_article/";
            return basePath + attachment.getFileUrl();  // 组合出文件的绝对路径

        }
        return null;

    }
}
