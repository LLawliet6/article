package com.LS.article.service;

import com.LS.article.pojo.ArticleAttachment;
import com.LS.article.pojo.ArticleHeadline;
import com.LS.article.pojo.vo.HeadlineDetailVo;
import com.LS.article.pojo.vo.HeadlinePageVo;
import com.LS.article.pojo.vo.HeadlineQueryVo;

import java.util.List;
import java.util.Map;

public interface ArticleHeadlineService {

    boolean isFavorited(Integer userId, Integer hid);
    void addFavorite(Integer userId, Integer hid);
    void removeFavorite(Integer userId, Integer hid);



    /**
     * 批量添加附件
     *
     * @param attachments 附件对象列表
     */
    void uploadAttachments(List<ArticleAttachment> attachments);
    /**
     * 分页查询文章列表
     * @param headlineQueryVo 查询参数对象
     * @return 分页结果，包含文章信息
     */
    Map<String, Object> findPage(HeadlineQueryVo headlineQueryVo);

    /**
     * 根据文章ID查询文章详情
     */
    HeadlineDetailVo findHeadlineDetail(int hid);

    /**
     * 添加新文章
     */
    int addArticleHeadline(ArticleHeadline articleHeadline);

    /**
     * 根据文章ID查找文章
     */
    ArticleHeadline findByHid(Integer hid);

    /**
     * 更新文章信息
     */
    int update(ArticleHeadline articleHeadline);

    /**
     * 删除指定ID的文章
     */
    int removeByHid(int hid);

    /**
     * 根据类别ID查找文章列表
     */
    List<ArticleHeadline> findArticlesByTypeId(int typeId);

    List<ArticleAttachment> getAttachmentsByHid(int hid);

    List<HeadlinePageVo> getMyFavorites(Integer userId);
}
