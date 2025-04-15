package com.LS.article.service;

import com.LS.article.pojo.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getMenusByRole(Integer roleId);
    Integer getRoleIdByUserId(Integer userId);
}
