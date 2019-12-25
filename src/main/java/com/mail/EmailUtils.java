package com.mail;

import com.thread.ApiThreadTest;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;

import java.io.File;

public class EmailUtils {

    /**
     * @param filePath 邮件附件路径，可变参数(0-N)
     * @throws Exception
     */
    public static void sendMsg(String... filePath) throws Exception {

        // HtmlEmail mail = new HtmlEmail();
        HtmlEmail mail = new HtmlEmail();

        String htmlTemplate = "图片，请查收 "
                + "\n <img class=\"s_lg_img_gold_show\" src=\"https://dss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top-e3b63a0b1b.png\" alt=\"到百度首页\" title=\"到百度首页\">";

        // 解析本地图片和网络图片都有的html文件重点就是下面这两行；
        // ImageHtmlEmail通过setDataSourceResolver来识别并嵌入图片；
        // 查看DataSourceResolver的继承结构发现有几个好用的子类
//        DataSourceResolver[] dataSourceResolvers = new DataSourceResolver[] {new DataSourceFileResolver(), // 添加 DataSourceFileResolver用于解析本地图片
//                new DataSourceUrlResolver(new URL("http://")) // 添加 DataSourceUrlResolver用于解析网络图片，注意 new URL("http://")
//        };
        // DataSourceCompositeResolver类可以加入多个DataSourceResolver
        // 把需要的 DataSourceResolver放进数组传进去即可
//        mail.setDataSourceResolver(new DataSourceCompositeResolver(dataSourceResolvers));

        String[] toList = {"272137499@qq.com", "leonadozki@163.com"};
        mail.setHostName("smtp.163.com");  // 邮件服务器域名
        mail.setAuthentication("leonadozki@163.com", "52miaomiao"); // 邮箱账户
        mail.setCharset("UTF-8");
        mail.setFrom("leonadozki@163.com");
        for (String to : toList){
            mail.addTo(to);
        }
        mail.setSubject(ApiThreadTest.getDate() + "测试结果邮件");
        mail.setHtmlMsg("测试结果，请查收");

        // 附件处理
        for (String path: filePath){
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(path);
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("结果报告");
            // 没有附件判空，避免空指针
            if (path != null){
                File file = new File(path);
                attachment.setName(file.getName());
                mail.attach(attachment);
            }
        }
        String rString = mail.send();
        System.out.println(rString);
    }

    public static void main(String[] args) {
        try {
            sendMsg(ApiThreadTest.resultFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
