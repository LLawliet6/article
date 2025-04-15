package com.LS.article.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Menu {
    private Integer id;       // 菜单ID
    private String url;       // 页面路径
    private String name;      // 英文标识
    private Integer pid;      // 父菜单ID（0 表示一级菜单）
    private Integer orderId;  // 排序字段
    private String icon;      // 图标类名
    private String title;     // 中文名称

    private List<Menu> children;  // 子菜单列表（用于构建树）


}
