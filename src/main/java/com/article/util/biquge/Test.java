package com.article.util.biquge;

import com.article.dto.Content;
import com.article.dto.WeatherDTO;
import com.article.util.CommonUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.swing.filechooser.FileSystemView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 夜雨寒笙 on 2020/7/8 11:23
 */
public class Test {

    public static void main(String[] args) {


        List<WeatherDTO> weatherDTOS = Lists.newArrayList();
        Document document = Jsoup.parse(getA());

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
        transferToTxt(weatherDTOS);
    }

    private static void transferToTxt(List<WeatherDTO> weatherDTOS){
        weatherDTOS.sort(Comparator.comparing(WeatherDTO::getDate));
        //获取桌面路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com=fsv.getHomeDirectory();
        String filePath = com.getPath()+"\\weather";
        File out = new File(filePath+"\\out.txt");
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String addYinHao(String str) {
        return "\""+str+"\"";
    }

    private static String getA() {
        String a = "<!DOCTYPE html\n" +
                "\tPUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "\n" +
                "<head>\n" +
                "\t<title>\n" +
                "\t\t广州历史天气预报查询_2018年1月份广州天气记录_广州2018年1月份天气情况_天气后报\n" +
                "\t</title>\n" +
                "\t<link href=\"../../css/g.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\" />\n" +
                "\t<script src=\"http://dup.baidustatic.com/js/ds.js\"></script>\n" +
                "\t<meta name=\"Keywords\" content=\"广州历史天气,广东广州2018年1月份天气预报查询,过去天气,广州天气记录,历史气温,天气后报\" />\n" +
                "\t<meta name=\"description\" content=\"天气后报网提供广东广州历史天气查询,广州2018年1月份天气预报查询,以往天气记录包含最高温度、最低气温、天气情况、风力风向等历年气候指标\" />\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\t<form name=\"form1\" method=\"post\" action=\"/lishi/guangzhou/month/201801.html\" id=\"form1\">\n" +
                "\t\t<div>\n" +
                "\t\t\t<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"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\" />\n" +
                "</div>\n" +
                "\n" +
                "\t\t\t<div>\n" +
                "\t\t\t\t<div id=\"top\">\n" +
                "\t\t\t\t\t<div class=\"bd doc\">\n" +
                "\t\t\t\t\t\t<a href=\"http://www.tianqihoubao.com/\" class=\"home\">天气后报首页</a> | <a\n" +
                "\t\t\t\t\t\t\thref=\"http://www.tianqihoubao.com/tqhb/index.aspx\" target=\"_blank\">客户端下载</a> |<a\n" +
                "\t\t\t\t\t\t\thref=\"http://www.tianqihoubao.com/page/jz.htm\" target=\"_blank\"><b>赞助本站</b></a> |\n" +
                "\t\t\t\t\t\t<em style=\"color:Blue\"> 小提示：您可以搜索\"城市名+天气后报\"访问本站（如：广州天气后报）</em>\n" +
                "\t\t\t\t\t\t<div class=\"r\">\n" +
                "\t\t\t\t\t\t\t<a href=\"http://www.tianqihoubao.com/news/342.html\" target=\"_blank\">天气后报微信</a>\n" +
                "\t\t\t\t\t\t\t<a href=\"http://www.mingzhuxiaoshuo.com/\" target=\"_blank\">小说阅读</a>\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<!--导航 begin-->\n" +
                "\t\t\t\t<div class=\"nav_mod\">\n" +
                "\t\t\t\t\t<ul>\n" +
                "\t\t\t\t\t\t<li><a href=\"http://www.tianqihoubao.com\">首页</a></li>\n" +
                "\t\t\t\t\t\t<li><a class=\"current\" href=\"/lishi/\">历史天气</a></li>\n" +
                "\t\t\t\t\t\t<li><a href=\"/guoji/\" title=\"国际历史天气查询\">国际天气</a></li>\n" +
                "\t\t\t\t\t\t<li><a href=\"/yubao/\">天气预报</a></li>\n" +
                "\t\t\t\t\t\t<li><a href=\"/aqi/\" title=\"AQI-PM2.5查询\">空气质量</a></li>\n" +
                "\t\t\t\t\t\t<li><a href=\"/news/\" title=\"天气新闻\">天气新闻</a></li>\n" +
                "\t\t\t\t\t\t<li><a href=\"/tqhb/index.aspx\" target=\"_blank\" title=\"历史天气查询软件\">天气后报</a></li>\n" +
                "\t\t\t\t\t\t<li><a href=\"/qihou/\">气候信息</a></li>\n" +
                "\t\t\t\t\t</ul>\n" +
                "\t\t\t\t\t<span class=\"n_r\"></span>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<!--导航 end-->\n" +
                "\t\t\t\t<div id=\"mnav\" class=\"doc\">\n" +
                "\t\t\t\t\t<div class=\"hd\">\n" +
                "\t\t\t\t\t\t<a href=\"http://www.tianqihoubao.com/\">天气后报</a>&gt; <a href=\"/lishi/guangdong.htm\">\n" +
                "\t\t\t\t\t\t\t广东历史天气</a> &gt;<a href=\"/lishi/guangzhou.html\" title=\"广州历史天气预报\">广州历史天气</a>\n" +
                "\t\t\t\t\t\t&gt; <em>\n" +
                "                        2018年1月份广州天气</em></div>\n" +
                "\t\t\t\t\t<div class=\"bd\">\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class=\"doc\">\n" +
                "\t\t\t\t\t<script type=\"text/javascript\" src=\"http://www.tianqihoubao.com/AD/lishi_960_90.js\"></script>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div style=\"height:5px\"></div>\n" +
                "\t\t\t\t<div id=\"bd\" class=\"doc\">\n" +
                "\t\t\t\t\t<div class=\"hd\">\n" +
                "\t\t\t\t\t\t<div id=\"content\" class=\"wdetail\">\n" +
                "\t\t\t\t\t\t\t<h1>\n" +
                "\t\t\t\t\t\t\t\t广州历史天气预报 2018年1月份\n" +
                "\t\t\t\t\t\t\t</h1>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<div style=\"height:5px\"></div>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<table width=\"100%\" border=\"0\" class=\"b\" cellpadding=\"1\" cellspacing=\"1\">\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<b>日期</b></td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<b>天气状况</b></td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<b>气温</b></td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<b>风力风向</b></td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180101.html' title=\"2018年01月01日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月01日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t22℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t13℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180101.html' title=\"2018年01月01日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月01日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t22℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t13℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180102.html' title=\"2018年01月02日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月02日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t20℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t16℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180103.html' title=\"2018年01月03日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月03日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/阴</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t23℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t15℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180104.html' title=\"2018年01月04日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月04日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t阴\n" +
                "\t\t\t\t\t\t\t\t\t\t/小雨</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t23℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t14℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /北风 3～4级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180105.html' title=\"2018年01月05日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月05日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t阴\n" +
                "\t\t\t\t\t\t\t\t\t\t/小雨</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t19℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t13℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180106.html' title=\"2018年01月06日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月06日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t小雨-中雨\n" +
                "\t\t\t\t\t\t\t\t\t\t/中雨-大雨</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t15℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t12℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180107.html' title=\"2018年01月07日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月07日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t中雨-大雨\n" +
                "\t\t\t\t\t\t\t\t\t\t/中雨</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t15℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t7℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /北风 3～4级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180108.html' title=\"2018年01月08日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月08日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t中雨\n" +
                "\t\t\t\t\t\t\t\t\t\t/小雨-中雨</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t14℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t5℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t北风 4～5级\n" +
                "\t\t\t\t\t\t\t\t\t\t/北风 3～4级</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180109.html' title=\"2018年01月09日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月09日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t小雨\n" +
                "\t\t\t\t\t\t\t\t\t\t/阴</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t8℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t6℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t北风 3～4级\n" +
                "\t\t\t\t\t\t\t\t\t\t/北风 3～4级</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180110.html' title=\"2018年01月10日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月10日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t14℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t6℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180111.html' title=\"2018年01月11日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月11日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t16℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t6℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180112.html' title=\"2018年01月12日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月12日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t晴\n" +
                "\t\t\t\t\t\t\t\t\t\t/晴</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t16℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t7℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180113.html' title=\"2018年01月13日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月13日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t晴\n" +
                "\t\t\t\t\t\t\t\t\t\t/晴</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t19℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t7℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180114.html' title=\"2018年01月14日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月14日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t20℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t8℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180115.html' title=\"2018年01月15日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月15日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t21℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t11℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180116.html' title=\"2018年01月16日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月16日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t22℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t12℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180117.html' title=\"2018年01月17日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月17日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t25℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t13℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180118.html' title=\"2018年01月18日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月18日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/小雨</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t25℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t13℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180119.html' title=\"2018年01月19日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月19日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t24℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t16℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180120.html' title=\"2018年01月20日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月20日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t阴\n" +
                "\t\t\t\t\t\t\t\t\t\t/阴</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t23℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t15℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180121.html' title=\"2018年01月21日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月21日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t霾\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t24℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t16℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t微风 <3级 /微风 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180122.html' title=\"2018年01月22日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月22日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t霾\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t23℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t15℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t微风 <3级 /微风 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180123.html' title=\"2018年01月23日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月23日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t24℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t15℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180124.html' title=\"2018年01月24日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月24日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t23℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t16℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t东南风 3～4级\n" +
                "\t\t\t\t\t\t\t\t\t\t/东风 3～4级</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180125.html' title=\"2018年01月25日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月25日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/阴</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t23℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t12℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180126.html' title=\"2018年01月26日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月26日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t多云\n" +
                "\t\t\t\t\t\t\t\t\t\t/局部多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t19℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t11℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t北风 4\n" +
                "\t\t\t\t\t\t\t\t\t\t/北风 4</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180127.html' title=\"2018年01月27日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月27日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t阴\n" +
                "\t\t\t\t\t\t\t\t\t\t/阴</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t16℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t8℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /北风 3～4级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180128.html' title=\"2018年01月28日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月28日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t小雨\n" +
                "\t\t\t\t\t\t\t\t\t\t/小雨</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t11℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t5℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t北风 4～5级\n" +
                "\t\t\t\t\t\t\t\t\t\t/北风 4～5级</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180129.html' title=\"2018年01月29日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月29日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t小雨\n" +
                "\t\t\t\t\t\t\t\t\t\t/小雨</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t8℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t6℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180130.html' title=\"2018年01月30日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月30日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t小雨\n" +
                "\t\t\t\t\t\t\t\t\t\t/小雨</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t7℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t5℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t无持续风向 <3级 /无持续风向 <3级</td> </tr> <tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/20180131.html' title=\"2018年01月31日广州天气预报\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t2018年01月31日\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t阵雨\n" +
                "\t\t\t\t\t\t\t\t\t\t/多云</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t8℃\n" +
                "\t\t\t\t\t\t\t\t\t\t/\n" +
                "\t\t\t\t\t\t\t\t\t\t4℃\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\t北风 3～4级\n" +
                "\t\t\t\t\t\t\t\t\t\t/北风 3～4级</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t<div class=\"hr\">\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\t<a href=\"/lishi/guangzhou/month/201712.html\">前一月</a> <a\n" +
                "\t\t\t\t\t\t\t\t\thref=\"/lishi/guangzhou/month/201802.html\">\n" +
                "\t\t\t\t\t\t\t\t\t后一月</a> <a href=\"/lishi/guangzhou.html\" title=\"广州历史天气查询\">\n" +
                "\t\t\t\t\t\t\t\t\t广州历史天气</a> <a href=\"/lishi/guangzhou/1.html\">\n" +
                "\t\t\t\t\t\t\t\t\t广州1月份天气</a> <em style=\"font-size:12px\">搜索\"城市名+天气后报\"访问本站（广州天气后报）</em>\n" +
                "\t\t\t\t\t\t\t</p>\n" +
                "\t\t\t\t\t\t\t<div class=\"hr\">\n" +
                "\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<script type=\"text/javascript\" src=\"http://www.tianqihoubao.com/AD/lishi_640_60_bd.js\">\n" +
                "\t\t\t\t\t\t\t</script>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<h2>\n" +
                "\t\t\t\t\t\t\t\t其他月份</h2>\n" +
                "\t\t\t\t\t\t\t<div class=\"months\">\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/202007.html' title=\"2020年07月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2020年07月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/202006.html' title=\"2020年06月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2020年06月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/202005.html' title=\"2020年05月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2020年05月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/202004.html' title=\"2020年04月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2020年04月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/202003.html' title=\"2020年03月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2020年03月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/202002.html' title=\"2020年02月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2020年02月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/202001.html' title=\"2020年01月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2020年01月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201912.html' title=\"2019年12月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年12月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201911.html' title=\"2019年11月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年11月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201910.html' title=\"2019年10月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年10月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201909.html' title=\"2019年09月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年09月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201908.html' title=\"2019年08月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年08月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201907.html' title=\"2019年07月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年07月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201906.html' title=\"2019年06月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年06月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201905.html' title=\"2019年05月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年05月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201904.html' title=\"2019年04月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年04月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201903.html' title=\"2019年03月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年03月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201902.html' title=\"2019年02月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年02月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201901.html' title=\"2019年01月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2019年01月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201812.html' title=\"2018年12月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年12月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201811.html' title=\"2018年11月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年11月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201810.html' title=\"2018年10月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年10月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201809.html' title=\"2018年09月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年09月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201808.html' title=\"2018年08月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年08月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201807.html' title=\"2018年07月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年07月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201806.html' title=\"2018年06月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年06月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201805.html' title=\"2018年05月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年05月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201804.html' title=\"2018年04月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年04月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201803.html' title=\"2018年03月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年03月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201802.html' title=\"2018年02月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年02月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201801.html' title=\"2018年01月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2018年01月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201712.html' title=\"2017年12月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年12月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201711.html' title=\"2017年11月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年11月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201710.html' title=\"2017年10月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年10月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201709.html' title=\"2017年09月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年09月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201708.html' title=\"2017年08月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年08月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201707.html' title=\"2017年07月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年07月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201706.html' title=\"2017年06月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年06月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201705.html' title=\"2017年05月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年05月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201704.html' title=\"2017年04月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年04月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201703.html' title=\"2017年03月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年03月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201702.html' title=\"2017年02月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年02月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201701.html' title=\"2017年01月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2017年01月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201612.html' title=\"2016年12月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年12月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201611.html' title=\"2016年11月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年11月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201610.html' title=\"2016年10月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年10月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201609.html' title=\"2016年09月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年09月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201608.html' title=\"2016年08月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年08月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201607.html' title=\"2016年07月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年07月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201606.html' title=\"2016年06月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年06月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201605.html' title=\"2016年05月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年05月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201604.html' title=\"2016年04月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年04月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201603.html' title=\"2016年03月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年03月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201602.html' title=\"2016年02月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年02月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201601.html' title=\"2016年01月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2016年01月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201512.html' title=\"2015年12月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年12月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201511.html' title=\"2015年11月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年11月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201510.html' title=\"2015年10月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年10月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201509.html' title=\"2015年09月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年09月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201508.html' title=\"2015年08月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年08月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201507.html' title=\"2015年07月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年07月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201506.html' title=\"2015年06月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年06月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201505.html' title=\"2015年05月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年05月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201504.html' title=\"2015年04月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年04月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201503.html' title=\"2015年03月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年03月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201502.html' title=\"2015年02月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年02月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201501.html' title=\"2015年01月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2015年01月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201412.html' title=\"2014年12月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年12月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201411.html' title=\"2014年11月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年11月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201410.html' title=\"2014年10月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年10月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201409.html' title=\"2014年09月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年09月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201408.html' title=\"2014年08月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年08月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201407.html' title=\"2014年07月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年07月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201406.html' title=\"2014年06月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年06月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201405.html' title=\"2014年05月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年05月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201404.html' title=\"2014年04月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年04月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201403.html' title=\"2014年03月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年03月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201402.html' title=\"2014年02月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年02月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201401.html' title=\"2014年01月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2014年01月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201312.html' title=\"2013年12月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年12月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201311.html' title=\"2013年11月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年11月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201310.html' title=\"2013年10月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年10月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201309.html' title=\"2013年09月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年09月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201308.html' title=\"2013年08月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年08月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201307.html' title=\"2013年07月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年07月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201306.html' title=\"2013年06月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年06月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201305.html' title=\"2013年05月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年05月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201304.html' title=\"2013年04月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年04月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201303.html' title=\"2013年03月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年03月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201302.html' title=\"2013年02月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年02月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201301.html' title=\"2013年01月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2013年01月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201212.html' title=\"2012年12月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年12月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201211.html' title=\"2012年11月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年11月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201210.html' title=\"2012年10月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年10月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201209.html' title=\"2012年09月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年09月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201208.html' title=\"2012年08月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年08月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201207.html' title=\"2012年07月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年07月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201206.html' title=\"2012年06月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年06月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201205.html' title=\"2012年05月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年05月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201204.html' title=\"2012年04月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年04月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201203.html' title=\"2012年03月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年03月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201202.html' title=\"2012年02月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年02月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201201.html' title=\"2012年01月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2012年01月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201112.html' title=\"2011年12月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年12月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201111.html' title=\"2011年11月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年11月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201110.html' title=\"2011年10月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年10月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201109.html' title=\"2011年09月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年09月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201108.html' title=\"2011年08月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年08月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201107.html' title=\"2011年07月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年07月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201106.html' title=\"2011年06月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年06月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201105.html' title=\"2011年05月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年05月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201104.html' title=\"2011年04月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年04月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201103.html' title=\"2011年03月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年03月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201102.html' title=\"2011年02月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年02月</a>\n" +
                "\t\t\t\t\t\t\t\t<a href='/lishi/guangzhou/month/201101.html' title=\"2011年01月广州历史天气\">\n" +
                "\t\t\t\t\t\t\t\t\t2011年01月</a>\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t<div class=\"box p\">\n" +
                "\t\t\t\t\t\t\t<h2>\n" +
                "\t\t\t\t\t\t\t\t省份历史天气查询</h2>\n" +
                "\t\t\t\t\t\t\t<ul>\n" +
                "\t\t\t\t\t\t\t\t<li class=\"t h\">省份</li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/heilongjiang.htm'>黑龙江</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/jl.htm'>吉林</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/ln.htm'>辽宁</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/neimenggu.htm'>内蒙古</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/hebei.htm'>河北</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/shanxi.htm'>山西</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/sxi.htm'>陕西</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/shandong.htm'>山东</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/xinjiang.htm'>新疆</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/xizang.htm'>西藏</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/qinghai.htm'>青海</a> </li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/gansu.htm'>甘肃</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/ningxia.htm'>宁夏</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/henan.htm'>河南</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/jiangsu.htm'>江苏</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/hubei.htm'>湖北</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/zhejiang.htm'>浙江</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/anhui.htm'>安徽</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/fujian.htm'>福建</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/jiangxi.htm'>江西</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/hunan.htm'>湖南</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/guizhou.htm'>贵州</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/sichuan.htm'>四川</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/guangdong.htm'>广东</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/yunan.htm'>云南</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/guangxi.htm'>广西</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/hainan.htm'>海南</a></li>\n" +
                "\t\t\t\t\t\t\t</ul>\n" +
                "\t\t\t\t\t\t\t<ul>\n" +
                "\t\t\t\t\t\t\t\t<li class=\"t\">直辖市</li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/bj.htm'>北京</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/sh.htm'>上海</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/tj.htm'>天津</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/cq.htm'>重庆</a></li>\n" +
                "\t\t\t\t\t\t\t</ul>\n" +
                "\t\t\t\t\t\t\t<ul>\n" +
                "\t\t\t\t\t\t\t\t<li class=\"t\">港澳台</li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/xg.htm'>香港</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/am.htm'>澳门</a></li>\n" +
                "\t\t\t\t\t\t\t\t<li><a href='/lishi/taiwan.htm'>台湾</a></li>\n" +
                "\n" +
                "\t\t\t\t\t\t\t</ul>\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"bd\">\n" +
                "\t\t\t\t\t\t<!-- 广告位：300-250 -->\n" +
                "\t\t\t\t\t\t<script>\n" +
                "\t\t\t\t\t\t\t(function() {\n" +
                "    var s = \"_\" + Math.random().toString(36).slice(2);\n" +
                "    document.write('<div id=\"' + s + '\"></div>');\n" +
                "    (window.slotbydup=window.slotbydup || []).push({\n" +
                "        id: '4783230',\n" +
                "        container: s,\n" +
                "        size: '300,250',\n" +
                "        display: 'inlay-fix'\n" +
                "    });\n" +
                "})();\n" +
                "\t\t\t\t\t\t</script>\n" +
                "\n" +
                "\t\t\t\t\t\t<div style=\"height:10px\"></div>\n" +
                "\n" +
                "\t\t\t\t\t\t<div id=\"sad1\">\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t<div id=\"s-calder\" class=\"box\">\n" +
                "\t\t\t\t\t\t\t<h2>广州2018年1月份天气统计概况</h2>\n" +
                "\t\t\t\t\t\t\t<div class=\"con\">\n" +
                "\t\t\t\t\t\t\t\t月最高气温：25 ℃ (出现在2018-01-17) <br />月最低气温：4 ℃ (出现在2018-01-31) <br />\n" +
                "                            【<b>白天</b>】晴天：2 天；下雨：8 天；\n" +
                "\t\t\t\t\t\t\t\t多云、阴天：21 天；下雪：0 天。<br />\n" +
                "                             【<b>夜间</b>】晴天：2 天；下雨：9 天；\n" +
                "\t\t\t\t\t\t\t\t多云、阴天：21 天；下雪：0 天。<br />\n" +
                "                        </div>\n" +
                "\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t<script type=\"text/javascript\" src=\"http://www.tianqihoubao.com/AD/lishi_300_250_2.js\">\n" +
                "\t\t\t\t\t\t\t</script>\n" +
                "\n" +
                "\n" +
                "\t\t\t\t\t\t\t<div style=\"height:5px\"></div>\n" +
                "\t\t\t\t\t\t\t<div id=\"sad2\">\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t<div class=\"box news\">\n" +
                "\t\t\t\t\t\t\t\t<h3>广州天气新闻</h3>\n" +
                "\t\t\t\t\t\t\t\t<ul>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t<li><a href='/news/1195.html' title=\"广州春节天气_2017年广州春节天气预报\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\ttarget=\"_blank\">广州春节天气_2017年广州春节天气</a>&nbsp;(20161223)</li>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t<li><a href='/news/870.html' title=\"广州元旦天气_2017年广州元旦天气预报\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\ttarget=\"_blank\">广州元旦天气_2017年广州元旦天气</a>&nbsp;(20161222)</li>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t<li><a href='/news/532.html' title=\"广东广州高考天气_2016广州高考天气预报\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\ttarget=\"_blank\">广东广州高考天气_2016广州高考天</a>&nbsp;(20160524)</li>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t<li><a href='/news/194.html' title=\"广州端午节天气_广州2016年端午节天气\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\ttarget=\"_blank\">广州端午节天气_广州2016年端午节</a>&nbsp;(20160501)</li>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t</ul>\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t<div id=\"sad3\">\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<div class=\"box\">\n" +
                "\t\t\t\t\t\t\t\t<div class=\"box pcity\">\n" +
                "\t\t\t\t\t\t\t\t\t<h2>\n" +
                "\t\t\t\t\t\t\t\t\t\t主要城市天气预报查询</h2>\n" +
                "\t\t\t\t\t\t\t\t\t<ul>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/beijing.html'>北京</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/shanghai.html'>上海</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/tianjin.html'>天津</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/chongqing.html'>重庆</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/haerbin.html'>哈尔滨</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/changchun.html'>长春</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/shenyang.html'>沈阳</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/huhehaote.html'>呼和浩特</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/shijiazhuang.html'>石家庄</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/taiyuan.html'>太原</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/xian.html'>西安</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/jinan.html'>济南</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/wulumuqi.html'>乌鲁木齐</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/lasa.html'>拉萨</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/xining.html'>西宁</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/lanzhou.html'>兰州</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/yinchuan.html'>银川</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/zhengzhou.html'>郑州</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/nanjing.html'>南京</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/wuhan.html'>武汉</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/hangzhou.html'>杭州</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/hefei.html'>合肥</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/fujianfuzhou.html'>福州</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/nanchang.html'>南昌</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/changsha.html'>长沙</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/guiyang.html'>贵阳</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/chengdu.html'>成都</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/guangzhou.html'>广州</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/kunming.html'>昆明</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/nanning.html'>南宁</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t\t<li><a href='/yubao/shenzhen.html'>深圳</a></li>\n" +
                "\t\t\t\t\t\t\t\t\t</ul>\n" +
                "\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t<div id=\"sad4\">\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t<script type=\"text/javascript\" src=\"http://www.tianqihoubao.com/AD/lishi_300_250_3.js\">\n" +
                "\t\t\t\t\t\t\t</script>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<div class=\"box\">\n" +
                "\t\t\t\t\t\t\t\t<h3>广州简介</h3>\n" +
                "\t\t\t\t\t\t\t\t<div class=\"con\">\n" +
                "\t\t\t\t\t\t\t\t\t邮政编码：510000<br />\n" +
                "                            电话区号：<br />\n" +
                "\t\t\t\t\t\t\t\t\t<a href=\"#\">查看详情</a> <a href=\"/yubao/guangzhou.html\" target=\"_blank\">\n" +
                "\t\t\t\t\t\t\t\t\t\t广州天气</a>\n" +
                "\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t<div id=\"sad0\"></div>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<script async src=\"//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js\"></script>\n" +
                "\t\t\t\t\t\t\t<!-- tqhb_r_link190224 -->\n" +
                "\t\t\t\t\t\t\t<ins class=\"adsbygoogle\" style=\"display:block\" data-ad-client=\"ca-pub-4334685396432654\"\n" +
                "\t\t\t\t\t\t\t\tdata-ad-slot=\"8152614508\" data-ad-format=\"link\" data-full-width-responsive=\"true\"></ins>\n" +
                "\t\t\t\t\t\t\t<script>\n" +
                "\t\t\t\t\t\t\t\t(adsbygoogle = window.adsbygoogle || []).push({});\n" +
                "\t\t\t\t\t\t\t</script>\n" +
                "\t\t\t\t\t\t\t<!-- <img src=\"http://www.tianqihoubao.com/images/tianqi.jpg\"  width=\"300\" height=\"360\">-->\n" +
                "\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div id=\"ftad\" class=\"doc\">\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div id=\"ft\">\n" +
                "\t\t\t\t\t\t<div class=\"doc\">\n" +
                "\t\t\t\t\t\t\t<p><a href=\"/lishi/\">历史天气查询</a> |<a href=\"/aqi/\">空气质量指数(AQI)</a> |\n" +
                "\t\t\t\t\t\t\t\t<a href=\"/page/about.htm\" rel=\"nofollow\">关于我们</a> | <a href=\"/page/mianze.htm\"\n" +
                "\t\t\t\t\t\t\t\t\trel=\"nofollow\">\n" +
                "\t\t\t\t\t\t\t\t\t免责声明</a> | <a href=\"/page/link.htm\">友情链接</a> | <a href=\"/page/sitemap.htm\">网站地图</a>\n" +
                "\t\t\t\t\t\t\t\t| <a target=\"_blank\" href=\"http://wpa.qq.com/msgrd?v=3&uin=1968509554&site=qq&menu=yes\">\n" +
                "\t\t\t\t\t\t\t\t\t<img border=\"0\" src=\"http://wpa.qq.com/pa?p=2:1968509554:41 &r=0.9811456883326173\"\n" +
                "                                alt=\"天气后报客服\" title=\"天气后报客服\"></a> |\n" +
                "\t\t\t\t\t\t\t</p>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\tCopyright 2012-2013 <a href=\"http://www.tianqihoubao.com/\">www.tianqihoubao.com</a> .\n" +
                "\t\t\t\t\t\t\t\tAll Rights Reserved <a href=\"http://www.miibeian.gov.cn/\"\n" +
                "\t\t\t\t\t\t\t\t\ttarget=\"_blank\">苏ICP备12028315号-2</a>\n" +
                "\t\t\t\t\t\t\t\t<script language=\"javascript\" type=\"text/javascript\"\n" +
                "\t\t\t\t\t\t\t\t\tsrc=\"http://js.users.51.la/4560568.js\"></script>\n" +
                "\t\t\t\t\t\t\t\t<script type=\"text/javascript\">\n" +
                "\t\t\t\t\t\t\t\t\tvar _bdhmProtocol = ((\"https:\" == document.location.protocol) ? \" https://\" : \" http://\");\n" +
                "document.write(unescape(\"%3Cscript src='\" + _bdhmProtocol + \"hm.baidu.com/h.js%3Ff48cedd6a69101030e93d4ef60f48fd0' type='text/javascript'%3E%3C/script%3E\"));\n" +
                "\t\t\t\t\t\t\t\t</script>\n" +
                "\t\t\t\t\t\t\t\t<script type=\"text/javascript\" src=\"http://www.tianqihoubao.com/AD/lishi_xf.js\">\n" +
                "\t\t\t\t\t\t\t\t</script>\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t微信关注 天气后报 <img border=\"0\" src=\"http://www.tianqihoubao.com/images/e1.jpg\">查历史天气\n" +
                "                    </p>\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t</form>\n" +
                "\n" +
                "\n" +
                "\t<!-- Baidu Button BEGIN -->\n" +
                "\t<script type=\"text/javascript\" id=\"bdshare_js\" data=\"type=slide&img=6&pos=left&uid=653932\"></script>\n" +
                "\t<script type=\"text/javascript\" id=\"bdshell_js\"></script>\n" +
                "\t<script type=\"text/javascript\">\n" +
                "\t\tvar bds_config = {\"bdTop\":420};\n" +
                "\t\tdocument.getElementById(\"bdshell_js\").src = \"http://share.baidu.com/static/js/shell_v2.js?cdnversion=\" + new Date().getHours();\n" +
                "\t</script>\n" +
                "\t<!-- Baidu Button END -->\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        return a;
    }
}
