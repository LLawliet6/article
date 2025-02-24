package com.LS.article.service.impl;

import com.LS.article.dao.ArticleTypeDao;
import com.LS.article.dao.impl.ArticleTypeDaoImpl;
import com.LS.article.pojo.ArticleType;
import com.LS.article.service.ArticleTypeService;

import java.util.List;

public class ArticleTypeServiceImpl implements ArticleTypeService {
    private ArticleTypeDao typeDao = new ArticleTypeDaoImpl();

    @Override
    public List<ArticleType> findAll() {
        return typeDao.findAll();
    }
}

