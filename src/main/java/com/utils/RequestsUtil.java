package com.utils;

import com.domain.Api;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *  http请求工具类
 */
public class RequestsUtil {

    // 创建代理
    final static HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");

    // 配置代理
    static RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

    // 创建连接池， 使用final的变量Java和JVM会进行缓存，优化性能
    final static CloseableHttpClient httpClient = HttpClients.createDefault();


    /**
     *  实现基于httpclient的get请求
     * @param url 请求地址
     * @throws Exception
     */
    public static void doGet(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        // 通过配置的代理来请求
        httpGet.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String resContent = EntityUtils.toString(entity);
        System.out.println(resContent);
    }

    /**
     * 实现基于httpclient的基本post请求
     * @param url 请求地址
     * @param map map格式入参
     * @throws Exception
     */
    public static void doPost(String url, Map<String, String> map) throws Exception {
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
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        System.out.println(EntityUtils.toString(responseEntity));
    }

    public static void main(String[] args) throws Exception {
        // 获取excel中所有接口信息
        List<Api> apis = ExcelUtil.readExcel2Api();
        for (Api api: apis){
            if ("get".equals(api.getMethod()) && api.getStatus() == 1){
                RequestsUtil.doGet(api.getUrl());
            }else if ("post".equals(api.getMethod()) && api.getStatus() == 1){
                // 入参转换为Map传入
                RequestsUtil.doPost(api.getUrl(), MapUtil.convertString2Map1(api.getParams()));
            }
        }
    }
}
