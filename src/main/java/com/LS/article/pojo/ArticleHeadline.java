package com.LS.article.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleHeadline {
    private Integer hid;
    private String title;//标题
    private String article;//内容
    private Integer type;//类别
    private Integer publisher;//发布者id
    private Integer pageViews;//浏览量
    private Date createTime;
    private Date updateTime;
    private Integer isDeleted;
    private String imageUrl;
}

