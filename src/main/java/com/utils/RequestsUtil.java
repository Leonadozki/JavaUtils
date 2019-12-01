package com.utils;

import com.domain.Api;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.List;


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


    public static void doGet(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        // 通过配置的代理来请求
        httpGet.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String resContent = EntityUtils.toString(entity);
        System.out.println(resContent);
    }

    public static void main(String[] args) throws Exception {
        List<Api> apis = ExcelUtil.readExcel2Api();
        for (Api api: apis){
            if ("get".equals(api.getMethod()) && api.getStatus() == 1){
                String url = api.getUrl();
                RequestsUtil.doGet(url);
            }
        }
    }
}
