package com.LS.article.service.impl;

import com.LS.article.dao.UserDao;
import com.LS.article.dao.impl.UserDaoImpl;
import com.LS.article.pojo.User;
import com.LS.article.service.UserService;
import com.LS.article.util.MD5Util;

public class UserServiceImpl implements UserService {
    private UserDao userDao =new UserDaoImpl();
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByUid(Integer userId) {
        return userDao.findByUid(userId);
    }

    @Override
    public Integer registUser(User registUser) {
        // 处理增加数据的业务
        // 将明文密码转换成密文密码
        registUser.setUserPwd(MD5Util.encrypt(registUser.getUserPwd()));

        return userDao.insertUser(registUser);
    }
}

