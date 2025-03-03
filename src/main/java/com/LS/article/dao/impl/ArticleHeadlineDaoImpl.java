package com.LS.article.dao.impl;

import com.LS.article.dao.ArticleHeadlineDao;
import com.LS.article.dao.BaseDao;
import com.LS.article.pojo.ArticleAttachment;
import com.LS.article.pojo.ArticleHeadline;
import com.LS.article.pojo.vo.HeadlineDetailVo;
import com.LS.article.pojo.vo.HeadlinePageVo;
import com.LS.article.pojo.vo.HeadlineQueryVo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArticleHeadlineDaoImpl extends BaseDao implements ArticleHeadlineDao {
    // 判断用户是否已经收藏了文章
    public boolean isFavorited(Integer userId, Integer hid) {
        String sql = "SELECT COUNT(*) FROM article_favorite WHERE uid = ? AND hid = ?";
        Integer count = baseQueryObject(Integer.class, sql, userId, hid);
        return count != null && count > 0;
    }

    // 添加收藏记录
    public void addFavorite(Integer userId, Integer hid) {
        String sql = "INSERT INTO article_favorite (uid, hid, create_time) VALUES (?, ?, ?)";
        baseUpdate(sql, userId, hid, LocalDateTime.now());
    }

    // 删除收藏记录
    public void removeFavorite(Integer userId, Integer hid) {
        String sql = "DELETE FROM article_favorite WHERE uid = ? AND hid = ?";
        baseUpdate(sql, userId, hid);
    }


    //根据hid获取附件列表
    @Override
    public List<ArticleAttachment> getAttachmentsByHid(int hid) {
        String sql = "SELECT aid, hid, file_name AS fileName, file_url AS fileUrl, upload_time AS uploadTime FROM article_attachment WHERE hid = ?";
        return baseQuery(ArticleAttachment.class, sql, hid);
    }

    // 获取当前用户收藏的文章列表
    @Override
    public List<HeadlinePageVo> getMyFavorites(Integer userId) {
        String sql = """
                SELECT
                    ah.hid,
                    ah.title,
                    ah.type,
                    at.tname AS typeName,
                    ah.page_views AS pageViews,
                    TIMESTAMPDIFF(HOUR, ah.create_time, NOW()) AS pastHours,
                    ah.publisher,
                    au.username AS publisherName,
                    ah.image_url AS imageUrl
                FROM
                    article_favorite af
                JOIN
                    article_headline ah ON af.hid = ah.hid
                JOIN
                    article_user au ON ah.publisher = au.uid
                JOIN
                    article_type at ON ah.type = at.tid
                WHERE
                    af.uid = ? AND ah.is_deleted = 0
                """;
        return baseQuery(HeadlinePageVo.class, sql, userId);
    }


    // 批量添加附件
    @Override
    public void addAttachments(List<ArticleAttachment> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            System.out.println("附件列表为空，跳过插入操作。");
            return;
        }
        String sql = "INSERT INTO article_attachment (hid, file_name, file_url, upload_time) VALUES (?, ?, ?, NOW())";
        List<Object[]> paramsList = new ArrayList<>();

        for (ArticleAttachment attachment : attachments) {
            System.out.println("准备插入附件：" + attachment);
            paramsList.add(new Object[]{
                    attachment.getHid(),
                    attachment.getFileName(),
                    attachment.getFileUrl()
            });
        }

        try {
            baseBatchUpdate(sql, paramsList);
            System.out.println("附件批量插入成功！");
        } catch (Exception e) {
            System.err.println("附件插入失败：" + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public List<ArticleHeadline> findArticlesByTypeId(int typeId) {
        String sql = "SELECT hid, title FROM article_headline WHERE type = ? AND (is_deleted = 0 OR is_deleted IS NULL)";
        return baseQuery(ArticleHeadline.class, sql, typeId);
    }

    @Override
    public List<HeadlinePageVo> findPageList(HeadlineQueryVo headlineQueryVo) {
        List<Object> params = new ArrayList<>();
//        String sql = """
//                select
//                    hid,
//                    title,
//                    type,
//                    page_views as pageViews,
//                    TIMESTAMPDIFF(HOUR, create_time, now()) as pastHours,
//                    publisher
//                from
//                    article_headline
//                where
//                    is_deleted = 0
//                """;
        String sql = """
                SELECT
                    ah.hid,
                    ah.title,
                    ah.type,
                    at.tname AS typeName,
                    ah.page_views AS pageViews,
                    TIMESTAMPDIFF(HOUR, ah.create_time, NOW()) AS pastHours,
                    ah.publisher,
                    au.username AS publisherName,
                    ah.image_url AS imageUrl
                FROM
                    article_headline ah
                JOIN
                    article_user au ON ah.publisher = au.uid
                JOIN
                    article_type at ON ah.type = at.tid
                WHERE
                    ah.is_deleted = 0
                """;

        if (headlineQueryVo.getType() != 0) {
            sql = sql.concat(" and type = ? ");
            params.add(headlineQueryVo.getType());
        }
        if (headlineQueryVo.getKeyWords() != null && !headlineQueryVo.getKeyWords().equals("")) {
            sql = sql.concat(" and title like ? ");
            params.add("%" + headlineQueryVo.getKeyWords() + "%");
        }

        sql = sql.concat(" order by pastHours ASC, page_views DESC ");
        sql = sql.concat(" limit ?, ? ");
        params.add((headlineQueryVo.getPageNum() - 1) * headlineQueryVo.getPageSize());
        params.add(headlineQueryVo.getPageSize());

        return baseQuery(HeadlinePageVo.class, sql, params.toArray());
    }


    @Override
    public int findPageCount(HeadlineQueryVo headlineQueryVo) {
        List<Object> params = new ArrayList<>();

        String sql = """
                select 
                    count(1)
                from 
                    article_headline
                where 
                    is_deleted = 0
                """;

        if (headlineQueryVo.getType() != 0) {
            sql = sql.concat(" and type = ? ");
            params.add(headlineQueryVo.getType());
        }
        if (headlineQueryVo.getKeyWords() != null && !headlineQueryVo.getKeyWords().equals("")) {
            sql = sql.concat(" and title like ? ");
            params.add("%" + headlineQueryVo.getKeyWords() + "%");
        }

        Long count = baseQueryObject(Long.class, sql, params.toArray());
        return count.intValue();
    }

    @Override
    public int incrPageViews(int hid) {
        String sql = "update article_headline set page_views = page_views + 1 where hid = ?";
        return baseUpdate(sql, hid);
    }

    public List<HeadlineDetailVo> findAllHeadlineDetails() {
        String sql = """
                select
                  h.hid as hid,
                  h.title as title,
                  h.type as type,
                  t.tname as typeName,
                  h.page_views as pageViews,
                  TIMESTAMPDIFF(HOUR, h.create_time, now()) as pastHours,
                  h.publisher as publisher      
                from 
                    article_headline h
                left join 
                    article_type t on h.type = t.tid
                left join
                    article_user u on h.publisher = u.uid
                """;

        List<HeadlineDetailVo> list = baseQuery(HeadlineDetailVo.class, sql);
        return list != null ? list : Collections.emptyList();
    }


    public HeadlineDetailVo findHeadlineDetail(int hid) {
        String sql = """
                select
                  h.hid as hid,
                  h.title as title,
                  h.article article ,
                  h.type as type,
                  t.tname as typeName,
                  h.page_views as pageViews,
                  TIMESTAMPDIFF(HOUR, h.create_time, now()) as pastHours,
                  h.publisher as publisher,
                  u.username as publisherName,
                  h.image_url as imageUrl
                from
                    article_headline h
                left join
                    article_type t on h.type = t.tid
                left join
                    article_user u on h.publisher = u.uid
                where
                    h.hid = ?
                """;

        List<HeadlineDetailVo> list = baseQuery(HeadlineDetailVo.class, sql, hid);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }


    //    @Override
//    public int addArticleHeadline(ArticleHeadline articleHeadline) {
//        String sql = """
//                insert into article_headline (title, article, type, publisher, page_views, create_time, update_time, is_deleted, image_url)
//                values (?, ?, ?, ?, 0, now(), now(), 0, ?)
//                """;
//        return baseUpdate(sql,
//                articleHeadline.getTitle(),
//                articleHeadline.getArticle(),
//                articleHeadline.getType(),
//                articleHeadline.getPublisher(),
//                articleHeadline.getImageUrl()
//        );
//    }
    public int addArticleHeadline(ArticleHeadline articleHeadline) {
        String sql = """
                insert into article_headline (title, article, type, publisher, page_views, create_time, update_time, is_deleted, image_url)
                values (?, ?, ?, ?, 0, now(), now(), 0, ?)
                """;
        baseUpdate(sql,
                articleHeadline.getTitle(),
                articleHeadline.getArticle(),
                articleHeadline.getType(),
                articleHeadline.getPublisher(),
                articleHeadline.getImageUrl()
        );

        // 查询最后插入的主键
        String querySql = "SELECT LAST_INSERT_ID()";
        return baseQueryObject(Integer.class, querySql);
    }


    @Override
    public ArticleHeadline findByHid(Integer hid) {
        String sql = """
                select
                    hid,
                    title,
                    article,
                    type,
                    publisher,
                    page_views as pageViews,
                    create_time as createTime,
                    update_time as updateTime,
                    is_deleted as isDeleted,
                    image_url as imageUrl
                from 
                    article_headline
                where 
                    hid = ?
                """;

        List<ArticleHeadline> list = baseQuery(ArticleHeadline.class, sql, hid);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    @Override
    public int update(ArticleHeadline articleHeadline) {

        String sql = """
                update 
                    article_headline
                set
                    title = ?,
                    article = ?,
                    type = ?,
                    update_time = now(),
                    image_url = ? -- 确保图片路径更新
                where 
                    hid = ?
                """;

        return baseUpdate(sql,
                articleHeadline.getTitle(),
                articleHeadline.getArticle(),
                articleHeadline.getType(),
                articleHeadline.getImageUrl(), // 更新图片路径
                articleHeadline.getHid()
        );

    }

    @Override
    public int removeByHid(int hid) {
        String sql = "update article_headline set is_deleted = 1 where hid = ?";
        return baseUpdate(sql, hid);
    }


}


