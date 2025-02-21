package com.LS.article.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeadlinePageVo implements Serializable {
    private Integer hid;   //文章id
    private String title;  //文章标题
    private Integer type;  //文章类型id
    private Integer pageViews;  //阅读量
    private Long pastHours;  //发布时间
    private Integer publisher;  //发布人id
    private String publisherName;  //发布人姓名
    private String typeName;  //文章类型名
    private String imageUrl;  //文章图片路径
}
