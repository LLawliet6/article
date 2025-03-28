package com.LS.article.dao;

import com.LS.article.pojo.ArticleAttachment;
import com.LS.article.pojo.ArticleHeadline;
import com.LS.article.pojo.vo.HeadlineDetailVo;
import com.LS.article.pojo.vo.HeadlinePageVo;
import com.LS.article.pojo.vo.HeadlineQueryVo;

import java.util.List;

public interface ArticleHeadlineDao {
    public boolean isFavorited(Integer userId, Integer hid);
    void addFavorite(Integer userId, Integer hid);
    void removeFavorite(Integer userId, Integer hid);



    /**
     * 批量添加附件
     *
     * @param attachments 附件对象列表
     */
   List<Integer> addAttachments(List<ArticleAttachment> attachments);

    /**
     * 根据类别ID查询文章列表
     */
    List<ArticleHeadline> findArticlesByTypeId(int typeId);

    /**
     * 分页查询文章列表
     */
    List<HeadlinePageVo> findPageList(HeadlineQueryVo headlineQueryVo);

    /**
     * 查询文章分页总数
     */
    int findPageCount(HeadlineQueryVo headlineQueryVo);

    /**
     * 增加文章浏览量
     */
    int incrPageViews(int hid);

    /**
     * 查找文章详情
     */
    HeadlineDetailVo findHeadlineDetail(int hid);

    /**
     * 查找所有文章详情
     */
    List<HeadlineDetailVo> findAllHeadlineDetails();

    /**
     * 添加新文章
     */
    int addArticleHeadline(ArticleHeadline articleHeadline);

    /**
     * 通过ID查找文章
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


    List<ArticleAttachment> getAttachmentsByHid(int hid);

    List<HeadlinePageVo> getMyFavorites(Integer userId);

    int cancelFavorite(int hid);

    // 添加单个附件
 int addAttachment(ArticleAttachment attachment);
}

