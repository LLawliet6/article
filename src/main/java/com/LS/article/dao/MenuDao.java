package com.LS.article.dao;

import com.LS.article.pojo.Menu;

import java.util.List;

public interface MenuDao {


    List<Menu> getMenusByRole(Integer roleId);
}
