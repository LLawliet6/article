package com.LS.article.util;

import com.LS.article.pojo.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuTreeUtil {

    public static List<Menu> buildTree(List<Menu> menuList) {
        List<Menu> tree = new ArrayList<>();
        Map<Integer, Menu> map = new HashMap<>();

        for (Menu menu : menuList) {
            map.put(menu.getId(), menu);
        }

        for (Menu menu : menuList) {
            if (menu.getPid() == 0) {
                tree.add(menu);
            } else {
                Menu parent = map.get(menu.getPid());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        }

        return tree;
    }
}
