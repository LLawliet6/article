package com.LS.article.dao;

import com.LS.article.pojo.User;

public interface UserDao {


    User findByUsername(String username);


    User findByUid(Integer userId);


    Integer insertUser(User registUser);
}

