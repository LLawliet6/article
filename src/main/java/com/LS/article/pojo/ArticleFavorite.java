package com.LS.article.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleFavorite {
    private Integer uid;
    private Integer hid;
    private Date createTime;
}
