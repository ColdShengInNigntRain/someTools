package com.article.util.biquge;

import com.article.constant.SearchWeb;
import com.article.dto.Title;
import com.article.util.CommonUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;


public class SearchFrom2KXS {

    public static void main (String[] args) {
        String searchName = "有匪";
        List<Title> titles = searchNovel(searchName);
        System.out.println(titles);
    }

    public static List<Title> searchNovel(String searchName) {

        StringBuilder sb = new StringBuilder();
        byte[] bytes = new byte[0];
        try {
            bytes = searchName.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (byte b : bytes) {
            sb.append("%").append(Integer.toHexString((b & 0xff)).toUpperCase());
        }

        String url = CommonUtil.transferToSafe(SearchWeb.KXS.getWebUrl().concat("/modules/article/search.php?searchtype=keywords&searchkey=").concat(sb.toString()));
        url = url.replace("&amp;", "\\&");
        Document document = CommonUtil.getDocumentByGet(url);
        //todo
        Elements bookbox = document.getElementsByClass("odd");
        if (bookbox.size() <= 0) {
            return Collections.emptyList();
        }

        List<Title> titleList = Lists.newArrayListWithCapacity(bookbox.size()/3);

        for (int i = 0;i < bookbox.size()/3;i ++) {
            //标题<b style="color:red">有匪</b>
            String href = bookbox.get(i*3).select("a").get(0).attr("href");

            String title = bookbox.get(i*3).select("a").get(0).toString();
            title = StringUtils.replaceAll(title, "<b style=\"color:red\">","");
            title = StringUtils.replaceAll(title, "</b>","");
            title = StringUtils.substringBetween(title, ">", "<");
            //作者
            String author = bookbox.get(i*3 + 1).toString();
            author = StringUtils.replaceAll(author, "<b style=\"color:red\">","");
            author = StringUtils.replaceAll(author, "</b>","");
            author = StringUtils.substringBetween(author, ">", "<");

            titleList.add(Title.builder().titleName(title).uri(href).author(author).build());
        }
        return titleList;
    }

}
