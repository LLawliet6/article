package com.LS.article.controller;

import com.LS.article.common.Result;
import com.LS.article.pojo.Menu;
import com.LS.article.service.MenuService;
import com.LS.article.service.UserService;
import com.LS.article.service.impl.MenuServiceImpl;
import com.LS.article.service.impl.UserServiceImpl;
import com.LS.article.util.JwtHelper;
import com.LS.article.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/menu/*")
public class MenuController extends BaseController {

    private MenuService menuService = new MenuServiceImpl();

    protected void getMenuByRole(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("token");
        Long userId = JwtHelper.getUserId(token);

        if (userId == null) {
            WebUtil.writeJson(resp, Result.build(null, 401, "未登录"));
            return;
        }
    // 获取当前用户role_id
        Integer roleId = menuService.getRoleIdByUserId(userId.intValue());
        if (roleId == null) {
            WebUtil.writeJson(resp, Result.build(null,403, "无角色信息"));
            return;
        }
        // 获取当前用户权限树
        List<Menu> menus = menuService.getMenusByRole(roleId);
        WebUtil.writeJson(resp, Result.ok(menus));
    }
}
