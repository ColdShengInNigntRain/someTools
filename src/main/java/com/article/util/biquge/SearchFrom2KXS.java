package com.article.util.biquge;

import com.article.constant.SearchWeb;
import com.article.dto.Title;
import com.article.util.CommonUtil;
import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class SearchFrom2KXS {

    public static void main (String[] args) {
        String searchName = "有匪";
        searchNovel(searchName);
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
        Document document = CommonUtil.getDocument(url);
        //todo
        Elements bookbox = document.getElementsByClass("bookinfo");
        if (bookbox.size() <= 0) {
            return Collections.emptyList();
        }

        List<Title> titleList = Lists.newArrayListWithCapacity(bookbox.size());
        Iterator<Element> iterator = bookbox.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String author = element.getElementsByClass("author").get(0).childNodes().get(0).toString();
            Element bookname = element.getElementsByClass("bookname").select("a").get(0);
            String href = bookname.attributes().get("href");
            String title = bookname.childNodes().get(0).toString();
            titleList.add(Title.builder().titleName(title).uri(href).author(author).build());
        }
        return titleList;
    }

}
