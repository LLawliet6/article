package com.LS.article.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeadlineQueryVo implements Serializable {
    private String keyWords; // 搜索关键字
    private Integer type ; // 文章类型id
    private Integer pageNum; // 当前页码
    private Integer pageSize; // 每页显示数量
}
