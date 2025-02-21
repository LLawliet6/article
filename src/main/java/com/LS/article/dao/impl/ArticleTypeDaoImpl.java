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
}


