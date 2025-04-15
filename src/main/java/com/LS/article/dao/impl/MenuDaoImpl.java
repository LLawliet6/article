package com.LS.article.dao.impl;

import com.LS.article.dao.BaseDao;
import com.LS.article.dao.MenuDao;
import com.LS.article.pojo.Menu;

import java.util.List;

public class MenuDaoImpl extends BaseDao implements MenuDao {

    @Override
    public List<Menu> getMenusByRole(Integer roleId) {
        String sql = """
                SELECT
                    m.id,
                    m.url,
                    m.name,
                    m.pid,
                    m.order_id AS orderId,
                    m.icon,
                    m.title
                FROM menu m
                JOIN role_menu rm ON m.id = rm.menu_id
                WHERE rm.role_id = ?
                ORDER BY m.order_id ASC
                """;
        return baseQuery(Menu.class, sql, roleId);
    }


}
