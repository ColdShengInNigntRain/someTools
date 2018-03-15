package com.article;

import org.apache.commons.lang3.StringUtils;

public class Test {

    public static void main(String[] args) {
        String tt = "088：攻气满满【第四更，求月票订阅打赏】";
        dealTitleName(tt, 88);
    }

    private static String dealTitleName(String titleName, int count) {
        if (!StringUtils.contains(titleName, "第") || !StringUtils.contains(titleName, "章")) {
            titleName = "第" + (count+1) + "章 "+ titleName;
        }
        return titleName;
    }
}
