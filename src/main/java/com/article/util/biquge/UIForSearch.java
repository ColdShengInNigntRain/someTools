package com.article.util.biquge;

import com.article.constant.SearchWeb;
import com.article.dto.Title;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author: jiangxinlei
 * @Time: 2018/2/23 11:16
 **/
public class UIForSearch {

    private static List<Title> titles = null;

    public static void main(String[] args) {
        buildSearchUI();
    }

    public static void buildSearchUI() {
        JFrame frame = new JFrame("搜索小说/作者");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        panel.setLayout(null);


        JLabel sourceLabel = new JLabel("选择来源");
        sourceLabel.setBounds(10,0,80,20);
        panel.add(sourceLabel);

        JComboBox<String> comboBox = new JComboBox<>();
        for (SearchWeb searchWeb : SearchWeb.values()) {
            if (!searchWeb.equals(SearchWeb.NONE)) {
                comboBox.addItem(searchWeb.getDesc());
            }
        }
        comboBox.setBounds(100,0,80,20);
        panel.add(comboBox);

        JLabel bookLabel = new JLabel("小说/作者");
        bookLabel.setBounds(10,20,80,25);
        panel.add(bookLabel);

        JTextField searchText = new JTextField(20);
        searchText.setBounds(100, 20, 165, 25);
        panel.add(searchText);

        JButton button = new JButton("搜索");
        button.setBounds(10, 50, 80, 25);
        panel.add(button);

        button.addActionListener(e -> {
            if (!CollectionUtils.isEmpty(titles)) {
                int max = panel.countComponents();
                for (int i = 5; i < max; i ++ ) {
                    panel.remove(5);
                }
                panel.revalidate();
            }
            SwingUtilities.invokeLater(() -> {
                String searchName  = searchText.getText();
                if (StringUtils.isBlank(searchName)) {
                    JOptionPane.showMessageDialog(null, "不输入东西不给搜(╯‵□′)╯︵┻━┻", "警告",JOptionPane.WARNING_MESSAGE);
                    return;
                }


                String selectedSource = comboBox.getSelectedItem().toString();
                if (StringUtils.isBlank(selectedSource)) {
                    JOptionPane.showMessageDialog(null, "未选择来源!应该不会报这个的!");
                    return;
                }

                SearchWeb selectedSrc = SearchWeb.getByDesc(selectedSource);
                if (selectedSrc.equals(SearchWeb.BIQUGE)) {
                    titles = SearchFromBiQuGe.searchNovel(searchName);
                } else if (selectedSrc.equals(SearchWeb.KXS)) {
                    titles = SearchFrom2KXS.searchNovel(searchName);
                } else if (selectedSrc.equals(SearchWeb.BIQUKE)) {
                    titles = SearchFromBiQuKe.searchNovel(searchName);
                } else if (selectedSrc.equals(SearchWeb.BIQUGE5200)) {
                    titles = SearchFromBiQuGe5200.searchNovel(searchName);
                } else if (selectedSrc.equals(SearchWeb.BIQUGETV)) {
                    titles = SearchFromBiQuGeTv.searchNovel(searchName);
                }

                if (CollectionUtils.isEmpty(titles)) {
                    JOptionPane.showMessageDialog(null, "搜索不到_(:з」∠)_");
                    return;
                }

                for (int i = 0; i< titles.size(); i++) {
                    Title title = titles.get(i);
                    JButton titleButton = new JButton(title.getTitleName());
                    titleButton.setBounds(10, 80+30*i, 80, 25);
                    panel.add(titleButton);
                    JLabel detailLabel = new JLabel(title.getTitleName()+" "+title.getAuthor());
                    detailLabel.setBounds(100, 80+30*i, 400, 25);
                    panel.add(detailLabel);
                    titleButton.addActionListener(e1 -> buildDownLoadUI(title.getTitleName(), title.getUri(), selectedSrc));
                }
            });

        });

        JScrollPane jsp = new JScrollPane();
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //把滚动条添加到容器里面
        panel.add(jsp);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void fetchNovel(String titleName, String uri, SearchWeb searchWeb) {
        boolean result = false;
        if (searchWeb.equals(SearchWeb.BIQUGE)) {
            result = FetchFromBiQuGe.fetchNovel(titleName, uri);
        } else if (searchWeb.equals(SearchWeb.KXS)) {
            result = FetchFrom2KXS.fetchNovel(titleName, uri);
        } else if (searchWeb.equals(SearchWeb.BIQUKE)) {
            result = FetchFromBiQuKe.fetchNovel(titleName, uri);
        } else if (searchWeb.equals(SearchWeb.BIQUGE5200)) {
            result = FetchFromBiQuGe5200.fetchNovel(titleName, uri);
        } else if (searchWeb.equals(SearchWeb.BIQUGETV)) {
            result = FetchFromBiQuGeTv.fetchNovel(titleName, uri);
        }

        if (result) {
            JOptionPane.showMessageDialog(null, "下载完成了!!!!撒花✿✿ヽ(°▽°)ノ✿");
        }
    }



    public static void buildDownLoadUI(String titleName, String uri, SearchWeb searchWeb) {
        JFrame.setDefaultLookAndFeelDecorated(false);
        JFrame frame = new JFrame("从"+searchWeb.getDesc()+"下载小说");
        //宽和高
        frame.setSize(350,250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);
        /*
         * 调用用户定义的方法并添加组件到面板
         */
        placeComponents(panel);
        frame.setVisible(true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fetchNovel(titleName, uri, searchWeb);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel downloadLabel = new JLabel("已完成下载!!!!看小说去吧!!!");
        downloadLabel.setBounds(100, 20, 200, 25);
        panel.add(downloadLabel);

        JLabel noteLabel = new JLabel(("文章将存储到当前桌面的articles文件夹"));
        noteLabel.setBounds(10, 80, 245, 25);
        panel.add(noteLabel);

        JButton openButton = new JButton("点击打开文件夹");
        openButton.setBounds(10, 110, 160, 25);
        panel.add(openButton);

        //获取桌面路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com=fsv.getHomeDirectory();
        String filePath = com.getPath()+"\\articles";

            openButton.addActionListener(e -> {
                try {
                    Runtime.getRuntime().exec("cmd /c start " + filePath);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
    }

}
