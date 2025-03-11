package com.LS.article.dao;

import com.LS.article.pojo.ArticleType;

import java.util.List;

public interface ArticleTypeDao {

    List<ArticleType> findAll();

    int addType(ArticleType articleType);

    int updateType(ArticleType articleType);

    int deleteType(Integer tid);
}
