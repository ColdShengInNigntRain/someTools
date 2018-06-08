package com.article.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 搜索网站枚举类
 * Created by 又有一个禽兽 on 2018/6/8.
 */
@AllArgsConstructor
@Getter
public enum SearchWeb {
    NONE("", "无匹配"),
    BIQUGE("http://www.biqugeg.com", "笔趣阁"),
    KXS("https://www.2kxs.com","2K小说");

    private String webUrl;
    private String desc;

    public static SearchWeb getByDesc(String desc) {
        for (SearchWeb searchWeb : values()) {
            if (searchWeb.getDesc().equalsIgnoreCase(desc)) {
                return searchWeb;
            }
        }
        return NONE;
    }

}