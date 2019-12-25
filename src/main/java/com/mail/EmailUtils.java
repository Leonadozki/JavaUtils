package com.mail;

import com.utils.ExcelUtil;
import org.apache.commons.mail.DataSourceResolver;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceCompositeResolver;
import org.apache.commons.mail.resolver.DataSourceFileResolver;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

import java.net.URL;

public class EmailUtils {

    public static void main(String[] args) {
        try {
            sendMsg(ExcelUtil.path_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMsg(String path) throws Exception {

        // HtmlEmail mail = new HtmlEmail();
        ImageHtmlEmail mail = new ImageHtmlEmail();

        String htmlTemplate = "图片，请查收 "
                + "\n <img class=\"s_lg_img_gold_show\" src=\"https://dss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top-e3b63a0b1b.png\" alt=\"到百度首页\" title=\"到百度首页\">";

        // 解析本地图片和网络图片都有的html文件重点就是下面这两行；
        // ImageHtmlEmail通过setDataSourceResolver来识别并嵌入图片；
        // 查看DataSourceResolver的继承结构发现有几个好用的子类
        DataSourceResolver[] dataSourceResolvers = new DataSourceResolver[] {new DataSourceFileResolver(), // 添加 DataSourceFileResolver用于解析本地图片
                new DataSourceUrlResolver(new URL("http://")) // 添加 DataSourceUrlResolver用于解析网络图片，注意 new URL("http://")
        };
        // DataSourceCompositeResolver类可以加入多个DataSourceResolver
        // 把需要的 DataSourceResolver放进数组传进去即可
        mail.setDataSourceResolver(new DataSourceCompositeResolver(dataSourceResolvers));

        String[] toList = {"272137499@qq.com", "leonadozki@163.com"};
        mail.setHostName("smtp.163.com");  // 邮件服务器域名
        mail.setAuthentication("leonadozki@163.com", "52miaomiao"); // 邮箱账户
        mail.setCharset("UTF-8");
        mail.setFrom("leonadozki@163.com");
        for (String to : toList){
            mail.addTo(to);
        }
        mail.setSubject("1225结果邮件");
        mail.setHtmlMsg(htmlTemplate);
        String rString = mail.send();
        System.out.println(rString);
    }
}
