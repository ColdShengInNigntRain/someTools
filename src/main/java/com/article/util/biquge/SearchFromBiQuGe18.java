package com.article.util.biquge;

import com.article.constant.SearchWeb;
import com.article.dto.Title;
import com.article.util.CommonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SearchFromBiQuGe18 {

    public static void main (String[] args) {
        String searchName = "天才魔法师与天然呆勇者";
        List<Title> titles = searchNovel(searchName);
        System.out.println(titles);
    }

    public static List<Title> searchNovel(String searchName) {
        if (StringUtils.isNotBlank(searchName) && searchName.length()>10) {
            searchName = searchName.substring(0, 10);
        }
        String url = CommonUtil.transferToSafe(SearchWeb.BIQUGE18.getWebUrl().concat("/home/search"));
        Map<String, String> param = Maps.newHashMap();
        param.put("action","search");
        param.put("q",searchName);
        Document document = CommonUtil.getDocumentByPost(url, param);

        String singleArticle = document.baseUri();
        if (!StringUtils.equalsIgnoreCase(singleArticle, url)) {
            return Lists.newArrayList(Title.builder().titleName(searchName).uri(singleArticle).author("").build());
        }


        Elements bookbox = document.getElementsByClass("item-cover");
        if (bookbox.size() < 1) {
            return Collections.emptyList();
        }

        List<Title> titleList = Lists.newArrayListWithCapacity(bookbox.size());
        Iterator<Element> iterator = bookbox.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            Element bookname = element.getElementsByTag("a").get(0);
            String href = bookname.attributes().get("href");
            String title = bookname.attributes().get("title");
            titleList.add(Title.builder().titleName(SearchWeb.BIQUGE18.getWebUrl().concat(title)).uri(href).author("").build());
        }
        return titleList;
    }

}
