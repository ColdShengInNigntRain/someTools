package com.article.util.biquge;

import com.article.constant.SearchWeb;
import com.article.dto.Title;
import com.article.util.CommonUtil;
import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class SearchFromBiQuKe {

    public static void main (String[] args) {
        String searchName = "emm";
        searchNovel(searchName);
    }

    public static List<Title> searchNovel(String searchName) {
        String url = CommonUtil.transferToSafe(SearchWeb.BIQUKE.getWebUrl().concat("//s.php?q=").concat(searchName));

        Document document = CommonUtil.getDocumentByGet(url);
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
