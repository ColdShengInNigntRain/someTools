package com.article.util.biquge;

import com.article.dto.Content;
import com.article.dto.Title;
import com.article.util.CommonUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 从2K小说网获取小说
 * @Author: 夜雨寒笙
 * @Time: 2018/2/12 14:38
 **/
public class FetchFrom2KXS {

    public static void main(String[] args) {
        String articleCode = "https://www.2kxs.com/93054/";
        String articleName = "有匪";
        fetchNovel(articleName,articleCode);

    }

    public static boolean fetchNovel(String articleName, String url) {
        //获取桌面路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com=fsv.getHomeDirectory();
        String filePath = com.getPath()+"\\articles";
        //消除不受信任的HTML(防止XSS攻击)
        url = CommonUtil.transferToSafe(url);

        Document document = CommonUtil.getDocumentByGet(url);

        String readUrl = document.getElementById("bt_1").select("a").attr("href");

        readUrl = CommonUtil.transferToSafe(readUrl);

        Document readDocument = CommonUtil.getDocumentByGet(readUrl);

        List<Title> titles = getTitles(readDocument);

        List<Content> contents = getContents(readUrl, titles);

        contents.sort(Comparator.comparing(Content::getTitleId));
        CommonUtil.writeToFile(articleName, contents, filePath);
        return true;
    }

    private static List<Content> getContents(String uri, List<Title> titles) {
        List<Content> contents = Lists.newArrayListWithCapacity(titles.size());
        titles.parallelStream().forEach(title -> {
            Content content = getContent(uri, title);
            if (content != null) {
                contents.add(content);
            }
        });
        return contents;
    }

    private static Content getContent(String uri, Title title) {
        String titleName = title.getTitleName();
        System.out.println(titleName+" "+(new Date()));
        Integer id = title.getId();
        String url = uri.concat(title.getUri());
        //消除不受信任的HTML(防止XSS攻击)
        url = CommonUtil.transferToSafe(url);

        Document document = CommonUtil.getDocumentByGet(url);

        Elements elements = document.getElementsByClass("Text");
        List<Node> nodes = elements.get(0).childNodes();
        StringBuilder sb = new StringBuilder();
        sb.append(titleName).append("\n");
        for (Node node : nodes) {
            String s = node.toString();

            if(StringUtils.isBlank(s)
                    || s.contains("</a>")
                    || s.contains("</font>")
                    || s.contains("</strong>")
                    || s.contains("</script>")) {
                continue;
            }

            s = StringUtils.replaceAll(s, "&nbsp;", " ");
            s = StringUtils.replaceAll(s, "<br>", "\n");
            sb.append(s).append("\n");
        }
        return Content.builder().titleId(id).title(titleName).content(sb.toString()).build();
    }

    private static List<Title> getTitles(Document doc) {
        List<Title> titles = Lists.newArrayList();
        Elements elements = doc.getElementsByClass("book").select("a");
        int count = 0;

        for (int i = 4;i<elements.size();i++) {
            Element element = elements.get(i);
            String titleName = element.childNodes().get(0).toString();
            String url = element.attributes().get("href");
            titles.add(Title.builder().id(count).titleName(dealTitleName(titleName, count)).uri(url).build());
            count ++;
        }
        return titles;
    }

    private static String dealTitleName(String titleName, int count) {
        if (!StringUtils.contains(titleName, "第") || !StringUtils.contains(titleName, "章")) {
            titleName = "第" + (count+1) + "章 "+ titleName;
        }
        return titleName;
    }


}
