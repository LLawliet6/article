package com.LS.article.dao;

import com.LS.article.pojo.User;

import java.util.List;

public interface UserDao {


    User findByUsername(String username);


    User findByUid(Integer userId);


    Integer insertUser(User registUser);

    Integer deleteUser(Integer userId);

    List<User> findAll();
}

