package com.LS.article.controller;

import com.LS.article.common.Result;
import com.LS.article.common.ResultCodeEnum;
import com.LS.article.pojo.User;
import com.LS.article.service.UserService;
import com.LS.article.service.impl.UserServiceImpl;
import com.LS.article.util.JwtHelper;
import com.LS.article.util.MD5Util;
import com.LS.article.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user/*")
public class UserController extends BaseController{
    private UserService userService = new UserServiceImpl();

    /**
     * 完成注册的业务接口
     */
    protected void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 接收JSON信息
      User registUser = WebUtil.readJson(req, User.class);
        // 调用服务层将用户信息存入数据
        Integer rows =userService.registUser(registUser);

        // 根据存入是否成功处理响应值
        Result result =Result.ok(null);
        if(rows == 0){
            result=Result.build(null,ResultCodeEnum.USERNAME_USED);
        }
        WebUtil.writeJson(resp,result);
    }

    /**
     * 校验用户名是否被占用
     */
    protected void checkUserName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取账号
        String username = req.getParameter("username");
       User user = userService.findByUsername(username);
        Result result =Result.ok(null);
        if(null != user){
            result=Result.build(null,ResultCodeEnum.USERNAME_USED);
            System.out.println("用户名重复");
        }

        WebUtil.writeJson(resp,result);
    }

    /**
     * 根据token口令获得用户信息实现
     */
    protected void getUserInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求中的token
        String token = req.getHeader("token");
        Result result = Result.build(null,ResultCodeEnum.NOTLOGIN);
        if(null != token && (!"".equals(token))){
            if (!JwtHelper.isExpiration(token)) {
                Integer userId = JwtHelper.getUserId(token).intValue();
                User articleUser =userService.findByUid(userId);
                if(null != articleUser){
                    //通过校验 查询用户信息放入Result
                    Map data =new HashMap();
                    articleUser.setUserPwd("");
                    data.put("loginUser",articleUser);

                    result = Result.ok(data);
                }
            }
        }
        WebUtil.writeJson(resp,result);
    }

    /**
     * 登录实现
     */
    protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 接收用户名和密码
        /*{
            "username":"liushuai", //用户名
            "userPwd":"123456"     //明文密码
        }*/
        User paramUser = WebUtil.readJson(req, User.class);

        // 调用服务层方法 实现登录
        User loginUser = userService.findByUsername(paramUser.getUsername());
        Result result = null;
        if(null != loginUser){
            if (MD5Util.encrypt(paramUser.getUserPwd()).equalsIgnoreCase(loginUser.getUserPwd())) {
                Map data =new HashMap();
                data.put("token",JwtHelper.createToken(loginUser.getUid().longValue()));
                result=Result.ok(data);
            }else{
                result=Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
            }
        }else {
            result= Result.build(null, ResultCodeEnum.USERNAME_ERROR);

        }

        // 向客户端响应登录验证信息
        WebUtil.writeJson(resp,result);
    }
}

