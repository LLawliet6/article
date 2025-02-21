package com.LS.article.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    private Integer cid;
    private Integer articleId;
    private String content;
    private Integer userId;
    private java.util.Date createTime;
    private String username;
    private Integer pid;
    private List<Comment> children;
    private String replyUsername="";
}


