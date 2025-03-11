package com.LS.article.service;

import com.LS.article.pojo.ArticleType;

import java.util.List;

public interface ArticleTypeService {
    /**
     * 查询所有文章类型的方法
     */
    List<ArticleType> findAll();

    /**
     * 添加分类
     */
    Integer addType(ArticleType articleType);

    Integer updateType(ArticleType articleType);

    Integer deleteType(Integer tid);
}
