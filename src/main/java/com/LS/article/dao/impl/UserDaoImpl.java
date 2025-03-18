package com.LS.article.dao.impl;

import com.LS.article.dao.BaseDao;
import com.LS.article.dao.UserDao;
import com.LS.article.pojo.User;

import java.util.List;

public class UserDaoImpl extends BaseDao implements UserDao {

    @Override
    public User findByUsername(String username) {
        String sql = """
                select
                    uid,
                    username,
                    user_pwd userPwd,role
                from
                    article_user
                where
                    username = ?
                """;
        List<User> UserList = baseQuery(User.class, sql, username);
        return UserList != null && UserList.size() > 0 ? UserList.get(0) : null;
    }

    @Override
    public User findByUid(Integer userId) {
        String sql = """
                select
                    uid,
                    username,
                    user_pwd userPwd,role
                from
                    article_user
                where
                    uid = ?
                """;
        List<User> UserList = baseQuery(User.class, sql, userId);
        return UserList != null && UserList.size() > 0 ? UserList.get(0) : null;

    }

    @Override
    public Integer insertUser(User registUser) {
        String sql = """
                insert into article_user values (DEFAULT,?,?)
                """;
        return baseUpdate(sql,
                registUser.getUsername(),
                registUser.getUserPwd()
        );
    }

    @Override
    public Integer deleteUser(Integer userId) {
        String sql = """
                delete from article_user where uid = ?
                """;
        return baseUpdate(sql, userId);
    }

    @Override
    public List<User> findAll() {
        String sql = """
                select
                    uid,
                    username,
                    user_pwd userPwd,role
                from
                    article_user where role=0
                """;
        return  baseQuery(User.class, sql);
    }
}

