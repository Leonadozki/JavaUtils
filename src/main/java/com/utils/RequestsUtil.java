package com.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *  http请求工具类
 */
public class RequestsUtil {

    // 创建代理， 使用final的变量Java和JVM会进行缓存，优化性能
    //  final static HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");

    // 配置代理
    //    static RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

    // 创建连接池
    static CloseableHttpClient httpClient = HttpClients.createDefault();


    /**
     *  实现基于httpclient的get请求
     * @param url 请求地址
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        // 通过配置的代理来请求
//        httpGet.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        // 判断返回值是否为成功
        if (response.getStatusLine().getStatusCode() == 200){
            String resContent = EntityUtils.toString(entity);
            System.out.println(resContent);
            // 返回json内容
            return resContent;
        }
        return "";
    }

    /**
     * 实现基于httpclient的基本post请求
     * @param url 请求地址
     * @param map map格式入参
     * @throws Exception
     */
    public static String doPost(String url, Map<String, String> map) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        // NameValuePair对象存放k,v的入参
        List<NameValuePair> list = new ArrayList<>();
        Set<String> keySet = map.keySet();
        for (String key: keySet){
            list.add(new BasicNameValuePair(key, map.get(key)));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");
        // 请求配置参数
        httpPost.setEntity(entity);
        return getResponse(httpPost);
    }

    /**
     *  入参为json串的post请求， 及header处理
     */
    public static String doPostJson(String url, String params, Map<String, String> headers) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        // 添加headers处理
        if (headers!=null){
            Set<String> strings = headers.keySet();
            for (String header: strings){
                httpPost.addHeader(header, headers.get(header));
            }
        }
        if (params!=null){
            // json入参直接用StringEntity处理
            StringEntity stringEntity =  new StringEntity(params, "utf-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
        }
        // 配置连接
//        httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
        return getResponse(httpPost);
    }

    private static String getResponse(HttpPost httpPost) throws IOException {
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String resp = EntityUtils.toString(entity);
        System.out.println(resp);
        if (response.getStatusLine().getStatusCode() == 200){
            return resp;
        }
        return "";
    }
}
