package com.LS.article.dao.impl;

import com.LS.article.dao.ArticleTypeDao;
import com.LS.article.dao.BaseDao;
import com.LS.article.pojo.ArticleType;

import java.util.List;

public class ArticleTypeDaoImpl extends BaseDao implements ArticleTypeDao {
    @Override
    public List<ArticleType> findAll() {
        String sql = "select tid, tname from article_type";
        return baseQuery(ArticleType.class, sql);
    }

    @Override
    public int addType(ArticleType articleType) {
        String sql = "insert into article_type (tname) values (?)";
        return baseUpdate(sql, articleType.getTname());
    }

    @Override
    public int updateType(ArticleType articleType) {
        String sql = "update article_type set tname = ? where tid = ?";
        return baseUpdate(sql, articleType.getTname(), articleType.getTid());
    }

    @Override
    public int deleteType(Integer tid) {
        String sql = "delete from article_type where tid = ?";
        return baseUpdate(sql, tid);
    }
}


