package com.LS.article.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttachmentVo {
    private Integer aid;          // 附件ID
    private Integer hid;          // 文章ID
    private String articleTitle;  // 所属文章标题
    private String fileName;      // 附件名称
    private String fileUrl;       // 附件存储路径
    private Date uploadTime; // 上传时间
}
