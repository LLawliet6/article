package com.LS.article.service;

import com.LS.article.pojo.User;

import java.util.List;

public interface UserService {
    /**
     * 根据用户登录的账号找用户新的方法
     * @return 找到返回User对象,找不到返回null
     */
    User findByUsername(String username);


    User findByUid(Integer userId);


    Integer registUser(User registUser);

    Integer removeUser(Integer userId);

    List<User> findAll();
}

