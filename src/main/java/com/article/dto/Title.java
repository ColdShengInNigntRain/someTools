package com.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小说章节DTO
 * @Author: jiangxinlei
 * @Time: 2018/2/12 15:20
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Title {

    private Integer id;

    private String uri;

    private String titleName;

    private String author;

}
