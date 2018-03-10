package com.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小说内容DTO
 * @Author: jiangxinlei
 * @Time: 2018/2/12 14:32
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Content {

    private Integer titleId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

}
