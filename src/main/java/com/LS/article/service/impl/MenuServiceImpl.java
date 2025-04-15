package com.LS.article.service.impl;

import com.LS.article.dao.MenuDao;
import com.LS.article.dao.UserDao;
import com.LS.article.dao.impl.MenuDaoImpl;
import com.LS.article.dao.impl.UserDaoImpl;
import com.LS.article.pojo.Menu;
import com.LS.article.service.MenuService;
import com.LS.article.util.MenuTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MenuServiceImpl implements MenuService {
    private MenuDao menuDao = new MenuDaoImpl();
    private UserDao userDao = new UserDaoImpl(); // 获取用户角色

    @Override
    public List<Menu> getMenusByRole(Integer roleId) {
        List<Menu> flatMenus = menuDao.getMenusByRole(roleId);
        return MenuTreeUtil.buildTree(flatMenus);
    }

    @Override
    public Integer getRoleIdByUserId(Integer userId) {
        return userDao.getRoleIdByUserId(userId);
    }
}