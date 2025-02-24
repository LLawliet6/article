package com.LS.article.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleAttachment {
    private Integer aid;        // 附件ID
    private Integer hid;        // 所属文章ID
    private String fileName;    // 附件原始文件名
    private String fileUrl;     // 附件路径
    private Date uploadTime;
}