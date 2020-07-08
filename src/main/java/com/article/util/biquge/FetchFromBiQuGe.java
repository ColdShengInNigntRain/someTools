package com.article.util.biquge;

import com.article.dto.Content;
import com.article.dto.Title;
import com.article.dto.WeatherDTO;
import com.article.util.CommonUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.swing.filechooser.FileSystemView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Watchable;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 从笔趣阁获取小说
 * @Author: 夜雨寒笙
 * @Time: 2018/2/12 14:38
 **/
public class FetchFromBiQuGe {

    private static String weatherUrl = "http://www.tianqihoubao.com/lishi/guangzhou/month/";

    public static void main(String[] args) {
        List<WeatherDTO> weatherDTOS = Lists.newArrayList();
//        for (int i = 2018; i < 2021; i ++) {
//            for (int j = 1; j < 13; j ++) {
//                String h = i + "";
//                if (j < 10) {
//                    h = h + "0" + j;
//                } else {
//                    h = h + j;
//                }
//                fetchNovel(h, weatherDTOS);
//            }
//        }
        fetchNovel("201801", weatherDTOS);
        try {
            transferToTxt(weatherDTOS);
        } catch (Exception e) {

        }

    }

    public static void fetchNovel(String title, List<WeatherDTO> weatherDTOS) {

        String url = weatherUrl+title+".html";
        //消除不受信任的HTML(防止XSS攻击)
        url = CommonUtil.transferToSafe(url);

        Document document = CommonUtil.getDocumentByGet(url);

        Elements elements = document.getElementsByClass("wdetail").get(0).getElementsByTag("table").get(0).select("tr");

        for (int i = 1; i < elements.size(); i ++ ){
            Element element = elements.get(i);
            Elements details = element.select("td");
            String date = details.get(0).select("a").html().replaceAll("[年|月|日]","");
            String weather = details.get(1).ownText();
            String temperature = details.get(2).ownText();
            String wind = details.get(3).ownText();
            weatherDTOS.add(WeatherDTO.builder().date(date).weather(weather).temperatue(temperature).wind(wind).build());
        }
        return;
    }

    private static void transferToTxt(List<WeatherDTO> weatherDTOS) throws Exception{
        weatherDTOS.sort(Comparator.comparing(WeatherDTO::getDate));
        //获取桌面路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com=fsv.getHomeDirectory();
        String filePath = com.getPath()+"\\weather";
        File out = new File(filePath+"\\out.txt");
        FileOutputStream fos = new FileOutputStream(out);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (int i = 0; i < weatherDTOS.size(); i ++ ) {
            WeatherDTO weatherDTO = weatherDTOS.get(i);
            String[] split = weatherDTO.getTemperatue().split("\\/");
            int a = 0;
            for (String s : split) {
                a = a + Integer.parseInt(s.replace("℃","").trim());
            }
            String tmp = a/split.length*1.0 + "";

            String writeStr =  addYinHao(weatherDTO.getDate())+" "+addYinHao(weatherDTO.getWeather())+" "
                    + addYinHao(weatherDTO.getTemperatue())+" "+addYinHao(tmp)+" "+addYinHao(weatherDTO.getWind());
            bw.write(writeStr);
            bw.newLine();
        }
        bw.close();
    }

    private static String addYinHao(String str) {
        return "\""+str+"\"";
    }


}
