package com.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部分网页会有下一页下一页
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentDTO {
    /**
     * 当前页面内容
     */
    private StringBuilder content;
    /**
     * 下一页的路径
     */
    private String nextUri;
}
