package com.article.util;

import com.article.dto.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @Author: 夜雨寒笙
 * @Time: 2018/2/23 17:28
 **/
public class CommonUtil {

    public static String transferToSafe(String url) {
        return Jsoup.clean(url, Whitelist.basic());
    }

    public static Document getDocumentByGet(String url) {
        Document doc = null;

        while (doc == null) {
            try {
                doc = Jsoup.connect(url).timeout(10000).get();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("重试一次");
                continue;
            }
            break;
        }
        return doc;
    }

    public static Document getDocumentByPost(String url, Map<String, String> param) {
        Document doc = null;

        while (doc == null) {
            try {
                doc = Jsoup.connect(url).timeout(10000).data(param).post();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("重试一次");
                continue;
            }
            break;
        }
        return doc;
    }

//    public static Document getDocumentByGet(String url) {
//        Document doc = null;
//        int num = 10;
//        while (num > 0 && doc == null) {
//            try {
//                doc = Jsoup.connect(url).timeout(10000).get();
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//                System.out.println("重试一次");
//                num --;
//                continue;
//            }
//            break;
//        }
//        return doc;
//    }

    public static void writeToFile(String articleName, List<Content> contents, String filePath) {
        makeDirs(filePath);
        try {
            FileOutputStream fos = new FileOutputStream(filePath+"/"+articleName+".txt");
            System.out.println("写入开始");
            for (Content content : contents) {
                fos.write(content.getContent().getBytes());
            }
            fos.close();
            System.out.println("写入结束");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void makeDirs(String filePath){
        File file = new File(filePath);
        if(!file.exists()) {
            file.mkdirs();
        }
        return;
    }

}
