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


public class SearchFromBiQuGe5200 {

    public static void main (String[] args) {
        String searchName = "天才魔法师与天然呆勇者";
        searchNovel(searchName);
    }

    public static List<Title> searchNovel(String searchName) {
        String url = CommonUtil.transferToSafe(SearchWeb.BIQUGE5200.getWebUrl().concat("/modules/article/search.php?searchkey=").concat(searchName));
        Document document = CommonUtil.getDocumentByGet(url);
        Elements bookbox = document.getElementsByTag("tr");
        if (bookbox.size() <= 1) {
            return Collections.emptyList();
        }

        List<Title> titleList = Lists.newArrayListWithCapacity(bookbox.size());
        Iterator<Element> iterator = bookbox.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            Elements td = element.getElementsByTag("td");
            if (td.size()<=0) {
                continue;
            }
            String author = td.get(2).toString();
            Element bookname = td.get(0).getElementsByTag("a").get(0);
            String href = bookname.attributes().get("href");
            String title = bookname.childNodes().get(0).toString();
            titleList.add(Title.builder().titleName(title).uri(href).author(author).build());
        }
        return titleList;
    }

}
